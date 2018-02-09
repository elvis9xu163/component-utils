package com.xjd.utils.component.retry.executor.trigger;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.xjd.utils.basic.AssertUtils;
import com.xjd.utils.component.retry.RetryScheduledExecutor;

/**
 * 倍增延时
 * @author elvis.xu
 * @since 2017-11-02 16:16
 */
public class ExponentialDelayTrigger<T> implements RetryScheduledExecutor.Trigger<T> {
	protected long delay;
	protected TimeUnit timeUnit;

	public ExponentialDelayTrigger(long delay, TimeUnit timeUnit) {
		AssertUtils.assertArgumentGreaterEqualThan(delay, 1L, "delay must greater than 1");
		AssertUtils.assertArgumentNonNull(timeUnit, "timeUnit cannot be null");
		this.delay = delay;
		this.timeUnit = timeUnit;
	}

	@Override
	public Date triggerTime(Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) {
		return new Date(System.currentTimeMillis() + (executeTimes == 1 ? 0 : timeUnit.toMillis((long)Math.pow(delay, (executeTimes - 1)))));
	}
}
