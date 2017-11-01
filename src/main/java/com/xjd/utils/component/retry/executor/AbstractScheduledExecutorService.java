package com.xjd.utils.component.retry.executor;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:22
 */
public abstract class AbstractScheduledExecutorService<T> extends AbstractRunnableFutureRetryExecutor<T> {

	protected ScheduledExecutorService scheduledExecutorService;

	public AbstractScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
	}
}
