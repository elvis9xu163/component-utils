package com.xjd.utils.component.retry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xjd.utils.component.retry.executor.DefaultScheduledExecutor;
import com.xjd.utils.component.retry.executor.ExecutorServiceExecutor;
import com.xjd.utils.component.retry.executor.scheduler.ScheduledExecutorServiceScheduler;
import com.xjd.utils.component.retry.executor.trigger.CustomDelayTrigger;
import com.xjd.utils.component.retry.executor.trigger.ExponentialDelayTrigger;
import com.xjd.utils.component.retry.executor.trigger.FixedDelayTrigger;
import com.xjd.utils.component.retry.executor.trigger.MultipleDelayTrigger;
import com.xjd.utils.component.retry.impl.DefaultAsyncRetry;
import com.xjd.utils.component.retry.impl.DefaultSyncRetry;
import com.xjd.utils.component.retry.judge.ExceptionInJudge;
import com.xjd.utils.component.retry.judge.HasExceptionAndInLimitTimesJudge;
import com.xjd.utils.component.retry.judge.HasExceptionJudge;
import com.xjd.utils.component.retry.judge.InLimitTimesJudge;

/**
 * @author elvis.xu
 * @since 2017-11-01 09:37
 */
public abstract class Retrys {

	public static <T> T syncRetry(Callable<T> task, RetryJudge<T> judge, RetryExecutor<T, T> executor) {
		return new DefaultSyncRetry<T>(executor).retry(task, judge);
	}

	public static <T> T syncRetry(Callable<T> task, RetryJudge<T> judge) {
		return new DefaultSyncRetry<T>(Executors.syncDirect()).retry(task, judge);
	}

	public static <T> T syncRetry(Callable<T> task, int mostTimes) {
		return new DefaultSyncRetry<T>(Executors.syncDirect()).retry(task, Judges.hasExceptionAndInLimitTimes(mostTimes));
	}

	public static <T> RetryFuture<T> asyncRetry(Callable<T> task, RetryJudge<T> judge, RetryExecutor<T, RetryFuture<T>> executor) {
		return new DefaultAsyncRetry<T>(executor).retry(task, judge);
	}

	public static <T> RetryFuture<T> asyncRetry(Callable<T> task, RetryJudge<T> judge, ExecutorService executorService) {
		return new DefaultAsyncRetry<T>(Executors.asyncExecutorService(executorService)).retry(task, judge);
	}

	public static <T> RetryFuture<T> asyncRetry(Callable<T> task, int mostTimes, ExecutorService executorService) {
		return new DefaultAsyncRetry<T>(Executors.asyncExecutorService(executorService)).retry(task, Judges.hasExceptionAndInLimitTimes(mostTimes));
	}

	public static <T> RetryFuture<T> retryScheduled(Callable<T> task, RetryJudge<T> judge, RetryScheduledExecutor.Scheduler<T> scheduler, RetryScheduledExecutor.Trigger<T> trigger) {
		return new DefaultAsyncRetry<T>(new DefaultScheduledExecutor<T>(scheduler, trigger)).retry(task, judge);
	}

	public static <T> RetryFuture<T> retryScheduled(Callable<T> task, RetryJudge<T> judge, ScheduledExecutorService scheduledExecutorService, RetryScheduledExecutor.Trigger<T> trigger) {
		return new DefaultAsyncRetry<T>(new DefaultScheduledExecutor<T>(new ScheduledExecutorServiceScheduler<T>(scheduledExecutorService), trigger)).retry(task, judge);
	}

	public static <T> RetryFuture<T> retryScheduled(Callable<T> task, int mostTimes, ScheduledExecutorService scheduledExecutorService, RetryScheduledExecutor.Trigger<T> trigger) {
		return new DefaultAsyncRetry<T>(new DefaultScheduledExecutor<T>(new ScheduledExecutorServiceScheduler<T>(scheduledExecutorService), trigger)).retry(task, Judges.hasExceptionAndInLimitTimes(mostTimes));
	}

	public static <T> RetryFuture<T> retryScheduledAtFixedDelay(Callable<T> task, int mostTimes, ScheduledExecutorService scheduledExecutorService, long delayInMilli) {
		return new DefaultAsyncRetry<T>(new DefaultScheduledExecutor<>(new ScheduledExecutorServiceScheduler<>(scheduledExecutorService), new FixedDelayTrigger<>(delayInMilli))).retry(task, Judges.hasExceptionAndInLimitTimes(mostTimes));
	}

	public static <T> RetryFuture<T> retryScheduledAtMultipleDelay(Callable<T> task, int mostTimes, ScheduledExecutorService scheduledExecutorService, long delayInMilli) {
		return new DefaultAsyncRetry<T>(new DefaultScheduledExecutor<>(new ScheduledExecutorServiceScheduler<>(scheduledExecutorService), new MultipleDelayTrigger(delayInMilli))).retry(task, Judges.hasExceptionAndInLimitTimes(mostTimes));
	}

	public static <T> RetryFuture<T> retryScheduledAtExponentialDelay(Callable<T> task, int mostTimes, ScheduledExecutorService scheduledExecutorService, long delay, TimeUnit timeUnit) {
		return new DefaultAsyncRetry<T>(new DefaultScheduledExecutor<>(new ScheduledExecutorServiceScheduler<>(scheduledExecutorService), new ExponentialDelayTrigger(delay, timeUnit))).retry(task, Judges.hasExceptionAndInLimitTimes(mostTimes));
	}

	public static <T> RetryFuture<T> retryScheduledAtCustomDelay(Callable<T> task, int mostTimes, ScheduledExecutorService scheduledExecutorService, long... delayInMillis) {
		return new DefaultAsyncRetry<T>(new DefaultScheduledExecutor<>(new ScheduledExecutorServiceScheduler<>(scheduledExecutorService), new CustomDelayTrigger(delayInMillis))).retry(task, Judges.hasExceptionAndInLimitTimes(mostTimes));
	}

	public static abstract class Judges {
		public static <T> RetryJudge<T> hasExceptionAndInLimitTimes(int mostTimes) {
			return new HasExceptionAndInLimitTimesJudge<>(mostTimes);
		}
		public static <T> RetryJudge<T> hasException() {
			return new HasExceptionJudge<>();
		}
		public static <T> RetryJudge<T> inLimitTimes(int limitTimes) {
			return new InLimitTimesJudge<>(limitTimes);
		}
		public static <T> RetryJudge<T> exceptionIn(Class<? extends Throwable>... throwableClasses) {
			return new ExceptionInJudge<>(throwableClasses);
		}
		public static <T> RetryJudge<T> exceptionNotIn(Class<? extends Throwable>... throwableClasses) {
			return not(exceptionIn(throwableClasses));
		}
		public static <T> RetryJudge<T> not(RetryJudge<T> retryJudge) {
			return RetryJudge.<T>not(retryJudge);
		}
	}

	public static abstract class Executors {
		public static <T> RetryExecutor<T, T> syncDirect() {
			return (task, times, result, throwable) -> task.call();
		}
		public static <T> RetryExecutor<T, RetryFuture<T>> asyncExecutorService(ExecutorService executorService) {
			return new ExecutorServiceExecutor(executorService);
		}
	}

	public static abstract class Schedulers {
		public static <T> RetryScheduledExecutor.Scheduler<T> scheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
			return new ScheduledExecutorServiceScheduler<T>(scheduledExecutorService);
		}
	}

	public static abstract class Triggers {
		public static <T> RetryScheduledExecutor.Trigger<T> fixedDelay(long delayInMilli) {
			return new FixedDelayTrigger<T>(delayInMilli);
		}
		public static <T> RetryScheduledExecutor.Trigger<T> multipleDelay(long delayInMilli) {
			return new MultipleDelayTrigger<T>(delayInMilli);
		}
		public static <T> RetryScheduledExecutor.Trigger<T> exponentialDelay(long delay, TimeUnit timeUnit) {
			return new ExponentialDelayTrigger<T>(delay, timeUnit);
		}
		public static <T> RetryScheduledExecutor.Trigger<T> customDelay(long... delayInMillis) {
			return new CustomDelayTrigger<T>(delayInMillis);
		}
	}
}
