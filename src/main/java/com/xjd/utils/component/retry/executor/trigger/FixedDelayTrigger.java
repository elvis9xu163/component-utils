package com.xjd.utils.component.retry.executor.trigger;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.xjd.utils.basic.AssertUtils;
import com.xjd.utils.component.retry.RetryScheduledExecutor;

/**
 * 固定延时
 * @author elvis.xu
 * @since 2017-11-02 16:16
 */
public class FixedDelayTrigger<T> implements RetryScheduledExecutor.Trigger<T> {
	protected long delay;
	protected TimeUnit timeUnit;

	public FixedDelayTrigger(long delayInMilli) {
		this(delayInMilli, TimeUnit.MILLISECONDS);
	}

	public FixedDelayTrigger(long delay, TimeUnit timeUnit) {
		AssertUtils.assertArgumentGreaterEqualThan(delay, 1L, "delay must greater than 1");
		AssertUtils.assertArgumentNonNull(timeUnit, "timeUnit cannot be null");
		this.delay = delay;
		this.timeUnit = timeUnit;
	}

	@Override
	public Date triggerTime(Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) {
		return new Date(System.currentTimeMillis() + timeUnit.toMillis(delay));
	}
}
