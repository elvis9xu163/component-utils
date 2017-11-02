package com.xjd.utils.component.retry.executor;

import org.springframework.scheduling.TaskScheduler;

import com.xjd.utils.basic.AssertUtils;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:22
 */
public abstract class SpringTaskSchedulerExecutor<T> extends AbstractRunnableExecutor<T> {

	protected TaskScheduler taskScheduler;

	public SpringTaskSchedulerExecutor(TaskScheduler taskScheduler) {
		AssertUtils.assertArgumentNonNull(taskScheduler, "taskScheduler cannot be null");
		this.taskScheduler = taskScheduler;
	}

	public TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}
}
