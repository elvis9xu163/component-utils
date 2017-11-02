package com.xjd.utils.component.retry;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.xjd.utils.basic.LockUtils;

/**
 * @author elvis.xu
 * @since 2017-11-01 10:04
 */
public interface AsyncRetry<T> extends Retry<T, RetryFuture<T>> {
	@Override
	default RetryFuture<T> retry(Callable<T> task, RetryJudge<T> judge) {
		RetryExecutor<T, RetryFuture<T>> executor = getAsyncExecutor();
		RetryState<T> state = new RetryState<>();
		RetryCallable<T> retryCallable = new RetryCallable<T>(state, task, judge, executor);

		while (true) {
			try (LockUtils.LockResource lr = LockUtils.lock(state.lock)) {
				state.t = null;
				state.currentExecuteTimes++;
				try {
					state.currentFuture = executor.execute(retryCallable, state.currentExecuteTimes, null, null);
					break;
				} catch (Exception e) {
					state.t = e;
				}
			}
			try {
				if (!judge.judge(task, state.currentExecuteTimes, null, state.t)) {
					state.finishLatch.countDown();
					break;
				}
			} catch (Exception e) { // judge异常，认为judge失败，不在重试
				state.t = e;
				state.finishLatch.countDown();
				break;
			}
		}
		return new MutRetryFuture<T>(state);
	}

	RetryExecutor<T, RetryFuture<T>> getAsyncExecutor();


	public static class RetryState<T> {
		protected Lock lock = new ReentrantLock();
		protected CountDownLatch finishLatch = new CountDownLatch(1);
		protected volatile int currentExecuteTimes = 0;
		protected volatile Exception t;
		protected volatile RetryFuture<T> currentFuture = null;
	}

	public static class MutRetryFuture<T> implements RetryFuture<T> {
		protected RetryState<T> state;

		public MutRetryFuture(RetryState<T> state) {
			this.state = state;
		}

		@Override
		public int getExecuteTimes() {
			return state.currentExecuteTimes;
		}

		@Override
		public T get() {
			try {
				state.finishLatch.await();
			} catch (InterruptedException e) {
				throw new RetryException.RetryInterruptedException(e);
			}
			if (state.currentFuture != null) {
				return state.currentFuture.get();
			}
			if (state.t != null) {
				throw new RetryException.RetryExecutionException(state.t);
			}
			return null;
		}

		@Override
		public T get(long timeout, TimeUnit unit) {
			try {
				if (state.finishLatch.await(timeout, unit)) {
					throw new RetryException.RetryTimeoutException();
				}
			} catch (InterruptedException e) {
				throw new RetryException.RetryInterruptedException(e);
			}
			if (state.currentFuture != null) {
				return state.currentFuture.get();
			}
			if (state.t != null) {
				throw new RetryException.RetryExecutionException(state.t);
			}
			return null;
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			if (state.currentFuture != null) {
				boolean cancel = state.currentFuture.cancel(mayInterruptIfRunning);
				if (cancel) { // 任务被取消
					state.finishLatch.countDown();
				}
				return cancel;
			}
			return false;
		}

		@Override
		public boolean isCancelled() {
			if (state.currentFuture != null) {
				return state.currentFuture.isCancelled();
			}
			return false;
		}

		@Override
		public boolean isDone() {
			return state.finishLatch.getCount() <= 0 && (state.currentFuture == null || state.currentFuture.isDone());
		}
	}

	public static class RetryCallable<T> implements Callable<T> {
		protected RetryState<T> state;
		protected Callable<T> task;
		protected RetryJudge<T> judge;
		protected RetryExecutor<T, RetryFuture<T>> executor;

		public RetryCallable(RetryState<T> state, Callable<T> task, RetryJudge<T> judge, RetryExecutor<T, RetryFuture<T>> executor) {
			this.state = state;
			this.task = task;
			this.judge = judge;
			this.executor = executor;
		}

		@Override
		public T call() throws Exception {
			T rt = null;
			Exception t = null;
			try {
				rt = task.call();
			} catch (Exception t1) {
				t = t1;
			}

			while (true) {
				try {
					if (judge.judge(task, state.currentExecuteTimes, rt, t)) {
						try (LockUtils.LockResource lr = LockUtils.lock(state.lock)) {
							state.currentExecuteTimes++;
							try {
								state.currentFuture = executor.execute(this, state.currentExecuteTimes, rt, t);
								break;
							} catch (Exception e) {
								t = e;
								rt = null;
							}
						}
					} else {
						state.finishLatch.countDown();
						break;
					}
				} catch (Exception t1) {
					// judge的过程出错，认为judge失败，不再执行，judge的失败
					t = t1;
					state.finishLatch.countDown();
					break;
				}
			}

			if (t != null) {
				throw t;
			}

			return rt;
		}
	}

}
