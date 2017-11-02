package com.xjd.utils.component.retry.executor.scheduler;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xjd.utils.basic.AssertUtils;

/**
 * @author elvis.xu
 * @since 2017-11-02 15:25
 */
public class ScheduledExecutorServiceScheduler<T> extends AbstractFutureScheduler<T> {
	protected ScheduledExecutorService scheduledExecutorService;

	public ScheduledExecutorServiceScheduler(ScheduledExecutorService scheduledExecutorService) {
		AssertUtils.assertArgumentNonNull(scheduledExecutorService, "scheduledExecutorService cannot be null");
		this.scheduledExecutorService = scheduledExecutorService;
	}

	public ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}

	@Override
	public Future<T> doSchedule(Date triggerTime, Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) throws Exception {
		long millis = triggerTime.getTime() - System.currentTimeMillis();
		if (millis < 0) millis = 0;
		return getScheduledExecutorService().schedule(task, millis, TimeUnit.MILLISECONDS);
	}
}
