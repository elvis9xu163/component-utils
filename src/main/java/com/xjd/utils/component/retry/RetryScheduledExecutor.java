package com.xjd.utils.component.retry;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author elvis.xu
 * @since 2017-11-02 14:34
 */
public interface RetryScheduledExecutor<T> extends RetryExecutor<T, RetryFuture<T>> {

	@Override
	default RetryFuture<T> execute(Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) throws Exception {
		Date triggerTime = getTrigger().triggerTime(task, executeTimes, lastResult, lastThrowable);
		return getScheduler().schedule(triggerTime, task, executeTimes, lastResult, lastThrowable);
	}

	Trigger<T> getTrigger();

	Scheduler<T> getScheduler();

	public static interface Scheduler<T> {
		RetryFuture<T> schedule(Date triggerTime, Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) throws Exception;
	}

	public static interface Trigger<T> {
		Date triggerTime(Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable);
	}
}
