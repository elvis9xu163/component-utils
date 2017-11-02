package com.xjd.utils.component.retry;

import java.util.Date;
import java.util.concurrent.*;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.xjd.utils.component.retry.executor.DefaultScheduledExecutor;
import com.xjd.utils.component.retry.executor.ScheduledExecutorServiceExecutor;
import com.xjd.utils.component.retry.executor.SpringTaskSchedulerExecutor;
import com.xjd.utils.component.retry.executor.scheduler.ScheduledExecutorServiceScheduler;
import com.xjd.utils.component.retry.executor.scheduler.SpringTaskSchedulerScheduler;
import com.xjd.utils.component.retry.executor.trigger.ExponentialDelayTrigger;
import com.xjd.utils.component.retry.executor.trigger.FixedDelayTrigger;
import com.xjd.utils.component.retry.executor.trigger.MultipleDelayTrigger;

/**
 * @author elvis.xu
 * @since 2017-11-01 18:00
 */
public class RetrysTest {
	@Test
	public void syncRetry() throws Exception {

		{
			String s = Retrys.syncRetry(() -> "XXX", Retrys.Judges.hasExceptionAndInLimitTimes(3), Retrys.Executors.syncDirect());
			System.out.println(s);
		}
		{
			String s = Retrys.syncRetry(() -> "HHH", Retrys.Judges.hasExceptionAndInLimitTimes(3));
			System.out.println(s);
		}
		{
			try {
				Retrys.syncRetry(()-> {throw new Exception("TTTT");}, 3);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		{
			try {
				Retrys.syncRetry(()-> {throw new IllegalArgumentException("YYY");}, Retrys.Judges.exceptionNotIn(IllegalArgumentException.class).and(Retrys.Judges.inLimitTimes(3)));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	@Test
	public void asyncRetry() throws Exception {
		{
			RetryFuture<String> future = Retrys.asyncRetry(() -> {
						System.out.println("执行完成.: " + System.currentTimeMillis());
						return "XXX";
					},
					Retrys.Judges.<String>not(Retrys.Judges.hasException()).and(Retrys.Judges.inLimitTimes(3)),
					Executors.newFixedThreadPool(3));
			String s = future.get();
			System.out.println(s);
		}
		{
			ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
			RetryFuture<String> future = Retrys.asyncRetry(() -> {
						System.out.println("执行完成.: " + System.currentTimeMillis());
						return "YYY";
					},
					Retrys.Judges.<String>not(Retrys.Judges.hasException()).and(Retrys.Judges.inLimitTimes(3)),
					new ScheduledExecutorServiceExecutor<String>(scheduledExecutorService) {
						@Override
						protected Future<String> doExecute(Callable<String> task, int executeTimes, String lastResult, Throwable lastThrowable) {
							System.out.println("执行开始" + executeTimes + ": " + System.currentTimeMillis());
							return getScheduledExecutorService().schedule(task, 3000L * executeTimes, TimeUnit.MILLISECONDS);
						}
					});
			String s = future.get();
			System.out.println(s);
		}
		{
			ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
			taskScheduler.setPoolSize(3);
			taskScheduler.initialize();
			RetryFuture<String> future = Retrys.asyncRetry(() -> {
						System.out.println("执行完成.: " + System.currentTimeMillis());
						return "ZZZ";
					},
					Retrys.Judges.<String>not(Retrys.Judges.hasException()).and(Retrys.Judges.inLimitTimes(3)),
					new SpringTaskSchedulerExecutor<String>(taskScheduler) {
						@Override
						protected Future<?> doExecute(Runnable task, int executeTimes, String lastResult, Throwable lastThrowable) {
							System.out.println("执行开始" + executeTimes + ": " + System.currentTimeMillis());
//							return getTaskScheduler().scheduleTime(task, (contxt) -> contxt.lastCompletionTime() != null ? null : new Date(System.currentTimeMillis() + 3000L * executeTimes));
							return getTaskScheduler().schedule(task, new Date(System.currentTimeMillis() + 3000L * executeTimes));
						}
					});
			String s = future.get();
			System.out.println(s);
		}

	}

	@Test
	public void scheduleRetry() {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
		{
			RetryFuture<String> future = Retrys.asyncRetry(() -> {
						System.out.println("执行完成.: " + System.currentTimeMillis());
						return "AAA";
					},
					Retrys.Judges.<String>not(Retrys.Judges.hasException()).and(Retrys.Judges.inLimitTimes(3)),
					new DefaultScheduledExecutor<String>(new ScheduledExecutorServiceScheduler<>(scheduledExecutorService), new FixedDelayTrigger(3000))
			);
			String s = future.get();
			System.out.println(s);
		}
		{
			ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
			taskScheduler.setPoolSize(3);
			taskScheduler.initialize();
			RetryFuture<String> future = Retrys.asyncRetry(() -> {
						System.out.println("执行完成.: " + System.currentTimeMillis());
						return "BBB";
					},
					Retrys.Judges.<String>not(Retrys.Judges.hasException()).and(Retrys.Judges.inLimitTimes(3)),
					new DefaultScheduledExecutor<>(new SpringTaskSchedulerScheduler<>(taskScheduler), new MultipleDelayTrigger(3000L)));
			String s = future.get();
			System.out.println(s);
		}
		{
			RetryFuture<String> future = Retrys.retryScheduled(() -> {
						System.out.println("执行完成.: " + System.currentTimeMillis());
						return "CCC";
					},
					Retrys.Judges.<String>not(Retrys.Judges.hasException()).and(Retrys.Judges.inLimitTimes(3)),
					scheduledExecutorService,
					new ExponentialDelayTrigger<String>(3L, TimeUnit.SECONDS)
					);
			String s = future.get();
			System.out.println(s);
		}
		{
			RetryFuture<String> future = Retrys.retryScheduledAtCustomDelay(() -> {
						System.out.println("执行完成.: " + System.currentTimeMillis());
						throw new Exception("我是异常");
					},
//					4,
					6,
					scheduledExecutorService,
					1000, 2000, 2000
					);
			try {
				System.out.println(future.get());
			} catch (Throwable t) {
				t.printStackTrace();
			}
			System.out.println("times: " + future.getExecuteTimes());
		}
	}
}