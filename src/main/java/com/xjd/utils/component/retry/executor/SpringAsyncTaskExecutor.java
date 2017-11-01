package com.xjd.utils.component.retry.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.core.task.AsyncTaskExecutor;

import com.xjd.utils.basic.AssertUtils;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:06
 */
public class SpringAsyncTaskExecutor<T> extends AbstractFutureRetryExecutor<T> {
	protected AsyncTaskExecutor asyncTaskExecutor;

	public SpringAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
		AssertUtils.assertArgumentNonNull(asyncTaskExecutor, "asyncTaskExecutor cannot be null");
		this.asyncTaskExecutor = asyncTaskExecutor;
	}

	@Override
	protected Future<T> doExecute(int executeTimes, Callable<T> task, T lastResult, Throwable lastThrowable) {
		return asyncTaskExecutor.submit(task);
	}
}
