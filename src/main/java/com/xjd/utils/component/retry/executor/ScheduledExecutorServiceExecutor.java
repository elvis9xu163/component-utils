package com.xjd.utils.component.retry.executor;

import java.util.concurrent.ScheduledExecutorService;

import com.xjd.utils.basic.AssertUtils;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:22
 */
public abstract class ScheduledExecutorServiceExecutor<T> extends AbstractFutureExecutor<T> {

	protected ScheduledExecutorService scheduledExecutorService;

	public ScheduledExecutorServiceExecutor(ScheduledExecutorService scheduledExecutorService) {
		AssertUtils.assertArgumentNonNull(scheduledExecutorService, "scheduledExecutorService cannot be null");
		this.scheduledExecutorService = scheduledExecutorService;
	}

	public ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}
}
