package com.xjd.utils.component.retry.executor;

import org.springframework.scheduling.TaskScheduler;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:22
 */
public abstract class AbstractSpringTaskScheduler<T> extends AbstractRunnableFutureRetryExecutor<T> {

	protected TaskScheduler taskScheduler;

	public AbstractSpringTaskScheduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

}
