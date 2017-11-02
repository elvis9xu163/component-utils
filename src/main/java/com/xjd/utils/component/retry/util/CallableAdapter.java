package com.xjd.utils.component.retry.util;

import java.util.concurrent.*;

/**
 * @author elvis.xu
 * @since 2017-11-02 15:37
 */
public class CallableAdapter<T> implements Runnable {
	Callable<T> callable;
	RunnableFuture<T> future;

	public CallableAdapter(Callable<T> callable, RunnableFuture<T> future) {
		this.callable = callable;
		this.future = future;
	}

	@Override
	public void run() {
		try {
			future.result = callable.call();
		} catch (Exception e) {
			future.t = e;
		}
	}

	public static class RunnableFuture<T> extends DelegateFuture<T> {

		protected T result;
		protected Exception t;

		public RunnableFuture() {
			super(null);
		}

		public RunnableFuture setFuture(Future future) {
			this.future = future;
			return this;
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
