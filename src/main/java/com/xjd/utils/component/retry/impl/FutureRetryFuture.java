package com.xjd.utils.component.retry.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.xjd.utils.component.retry.RetryException;
import com.xjd.utils.component.retry.RetryFuture;
import com.xjd.utils.component.retry.util.DelegateFuture;

/**
 * @author elvis.xu
 * @since 2017-11-01 10:08
 */
public class FutureRetryFuture<T> extends DelegateFuture<T> implements RetryFuture<T> {

	protected int executeTimes;

	public FutureRetryFuture(Future<T> future, int executeTimes) {
		super(future);
		this.executeTimes = executeTimes;
	}

	@Override
	public int getExecuteTimes() {
		return executeTimes;
	}

	@Override
	public T get() {
		try {
			return super.get();
		} catch (InterruptedException e) {
			throw new RetryException.RetryInterruptedException(e);

		} catch (ExecutionException e) {
			throw new RetryException.RetryExecutionException(e);
		}
	}

	@Override
	public T get(long timeout, TimeUnit unit) {
		try {
			return super.get(timeout, unit);
		} catch (InterruptedException e) {
			throw new RetryException.RetryInterruptedException(e);

		} catch (ExecutionException e) {
			throw new RetryException.RetryExecutionException(e);

		} catch (TimeoutException e) {
			throw new RetryException.RetryTimeoutException(e);
		}
	}
}
