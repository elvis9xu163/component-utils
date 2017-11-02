package com.xjd.utils.component.retry.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.xjd.utils.component.retry.util.CallableAdapter;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:22
 */
public abstract class AbstractRunnableExecutor<T> extends AbstractFutureExecutor<T> {

	@Override
	protected Future<T> doExecute(Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) throws Exception {
		CallableAdapter.RunnableFuture<T> future = new CallableAdapter.RunnableFuture<>();
		return future.setFuture(doExecute(new CallableAdapter<T>(task, future), executeTimes, lastResult, lastThrowable));
	}

	protected abstract Future<?> doExecute(Runnable task, int executeTimes, T lastResult, Throwable lastThrowable) throws Exception;


}
