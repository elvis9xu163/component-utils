package com.xjd.utils.component.retry.executor.trigger;

import java.util.Date;
import java.util.concurrent.Callable;

import com.xjd.utils.component.retry.RetryScheduledExecutor;

/**
 * 固定延时
 * @author elvis.xu
 * @since 2017-11-02 16:16
 */
public class CustomDelayTrigger<T> implements RetryScheduledExecutor.Trigger<T> {
	protected long[] delays;

	public CustomDelayTrigger(long... delayInMillis) {
		this.delays = delayInMillis;
	}


	@Override
	public Date triggerTime(Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) {
		return new Date(System.currentTimeMillis() + (executeTimes == 1 ? 0 : delays[executeTimes - 2]));
	}
}
