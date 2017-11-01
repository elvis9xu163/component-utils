package com.xjd.utils.component.retry.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.xjd.utils.component.retry.RetryExecutor;
import com.xjd.utils.component.retry.RetryFuture;
import com.xjd.utils.component.retry.impl.FutureRetryFuture;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:12
 */
public abstract class AbstractFutureRetryExecutor<T> implements RetryExecutor<T, RetryFuture<T>> {

	@Override
	public RetryFuture<T> execute(Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) {
		Future<T> future = doExecute(executeTimes, task, lastResult, lastThrowable);
		return new FutureRetryFuture(future, executeTimes);
	}

	protected abstract Future<T> doExecute(int executeTimes, Callable<T> task, T lastResult, Throwable lastThrowable);
}
