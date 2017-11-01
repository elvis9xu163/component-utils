package com.xjd.utils.component.retry.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.xjd.utils.basic.AssertUtils;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:09
 */
public class JdkExecutorService<T> extends AbstractFutureRetryExecutor<T> {
	protected ExecutorService executorService;

	public JdkExecutorService(ExecutorService executorService) {
		AssertUtils.assertArgumentNonNull(executorService, "executorService cannot be null");
		this.executorService = executorService;
	}

	@Override
	protected Future<T> doExecute(int executeTimes, Callable<T> task, T lastResult, Throwable lastThrowable) {
		return executorService.submit(task);
	}
}
