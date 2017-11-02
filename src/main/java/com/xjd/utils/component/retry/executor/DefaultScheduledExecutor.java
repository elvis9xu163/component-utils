package com.xjd.utils.component.retry.executor;

import com.xjd.utils.basic.AssertUtils;
import com.xjd.utils.component.retry.RetryScheduledExecutor;

/**
 * @author elvis.xu
 * @since 2017-11-02 15:19
 */
public class DefaultScheduledExecutor<T> implements RetryScheduledExecutor<T> {
	protected RetryScheduledExecutor.Scheduler<T> scheduler;
	protected RetryScheduledExecutor.Trigger trigger;

	public DefaultScheduledExecutor(Scheduler<T> scheduler, Trigger trigger) {
		AssertUtils.assertArgumentNonNull(scheduler, "scheduler cannot be null");
		AssertUtils.assertArgumentNonNull(trigger, "trigger cannot be null");
		this.scheduler = scheduler;
		this.trigger = trigger;
	}

	@Override
	public Trigger<T> getTrigger() {
		return trigger;
	}

	@Override
	public Scheduler<T> getScheduler() {
		return scheduler;
	}
}
