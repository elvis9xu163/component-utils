package com.xjd.utils.component.retry.executor.scheduler;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.scheduling.TaskScheduler;

import com.xjd.utils.basic.AssertUtils;
import com.xjd.utils.component.retry.util.CallableAdapter;

/**
 * @author elvis.xu
 * @since 2017-11-01 11:22
 */
public class SpringTaskSchedulerScheduler<T> extends AbstractFutureScheduler<T> {

	protected TaskScheduler taskScheduler;

	public SpringTaskSchedulerScheduler(TaskScheduler taskScheduler) {
		AssertUtils.assertArgumentNonNull(taskScheduler, "taskScheduler cannot be null");
		this.taskScheduler = taskScheduler;
	}

	public TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	@Override
	public Future<T> doSchedule(Date triggerTime, Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) throws Exception {
		CallableAdapter.RunnableFuture<T> future = new CallableAdapter.RunnableFuture<>();
		return future.setFuture(getTaskScheduler().schedule(new CallableAdapter<T>(task, future), triggerTime));
	}
}
