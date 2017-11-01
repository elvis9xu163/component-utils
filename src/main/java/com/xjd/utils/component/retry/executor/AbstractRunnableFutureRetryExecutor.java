package com.xjd.utils.component.retry.executor;

import java.util.concurrent.*;

import com.xjd.utils.component.retry.impl.DelegateFuture;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:22
 */
public abstract class AbstractRunnableFutureRetryExecutor<T> extends AbstractFutureRetryExecutor<T> {

	@Override
	protected Future<T> doExecute(int executeTimes, Callable<T> task, T lastResult, Throwable lastThrowable) {
		CallRunnableFuture<T> future = new CallRunnableFuture<>();
		Future<T> f = doExecute(executeTimes, () -> {

			try {
				future.result = task.call();
			} catch (Exception e) {
				future.t = e;
			}

		}, lastResult, lastThrowable);
		future.setFuture(f);
		return future;
	}

	protected abstract Future<T> doExecute(int executeTimes, Runnable task, T lastResult, Throwable lastThrowable);

	public static class CallRunnableFuture<T> extends DelegateFuture<T> {

		protected T result;
		protected Throwable t;

		public CallRunnableFuture() {
			super(null);
		}

		protected void setFuture(Future future) {
			this.future = future;
		}

		@Override
		public T get() throws InterruptedException, ExecutionException {
			super.get();

			if (t != null) {
				if (t instanceof RuntimeException) {
					throw (RuntimeException) t;
				}
				if (t instanceof InterruptedException) {
					throw (InterruptedException) t;
				}
				if (t instanceof ExecutionException) {
					throw (ExecutionException) t;
				}
				throw new ExecutionException(t);
			}

			return result;
		}

		@Override
		public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			super.get(timeout, unit);

			if (t != null) {
				if (t instanceof RuntimeException) {
					throw (RuntimeException) t;
				}
				if (t instanceof InterruptedException) {
					throw (InterruptedException) t;
				}
				if (t instanceof ExecutionException) {
					throw (ExecutionException) t;
				}
				if (t instanceof TimeoutException) {
					throw (TimeoutException) t;
				}
				throw new ExecutionException(t);
			}

			return result;
		}
	}
}
