package com.xjd.utils.component.retry.executor.scheduler;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.xjd.utils.component.retry.RetryFuture;
import com.xjd.utils.component.retry.RetryScheduledExecutor;
import com.xjd.utils.component.retry.impl.FutureRetryFuture;

/**
 * @author elvis.xu
 * @since 2017-11-02 15:30
 */
public abstract class AbstractFutureScheduler<T> implements RetryScheduledExecutor.Scheduler<T> {
	@Override
	public RetryFuture<T> schedule(Date triggerTime, Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) throws Exception {
		Future<T> future = doSchedule(triggerTime, task, executeTimes, lastResult, lastThrowable);
		return new FutureRetryFuture(future, executeTimes);
	}

	public abstract Future<T> doSchedule(Date triggerTime, Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) throws Exception;
}
