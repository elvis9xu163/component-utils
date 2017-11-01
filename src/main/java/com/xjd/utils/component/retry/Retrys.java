package com.xjd.utils.component.retry;

import java.util.concurrent.Callable;

import com.xjd.utils.component.retry.impl.DefaultSyncRetry;
import com.xjd.utils.component.retry.judge.*;

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

	public static <T> T syncRetryMostNTimes(Callable<T> task, int limitTimes) {
		return new DefaultSyncRetry<T>(Executors.syncDirect()).retry(task, Judges.hasExceptionAndInLimitTimes(limitTimes));
	}

	public static abstract class Judges {
		public static <T> RetryJudge<T> hasExceptionAndInLimitTimes(int limitTimes) {
			return new HasExceptionAndInLimitTimesJudge(limitTimes);
		}
		public static <T> RetryJudge<T> hasException() {
			return new HasExceptionJudge<>();
		}
		public static <T> RetryJudge<T> inLimitTimes(int limitTimes) {
			return new InLimitTimesJudge(limitTimes);
		}
		public static <T> RetryJudge<T> exceptionIn(Class<? extends Throwable>... throwableClasses) {
			return new ExceptionInJudge<T>(throwableClasses);
		}
		public static <T> RetryJudge<T> exceptionNotIn(Class<? extends Throwable>... throwableClasses) {
			return not(exceptionIn(throwableClasses));
		}
		public static <T> RetryJudge<T> not(RetryJudge<T> retryJudge) {
			return RetryJudge.not(retryJudge);
		}
	}

	public static abstract class Executors {
		public static <T> RetryExecutor<T, T> syncDirect() {
			return (task, times, result, throwable) -> task.call();
		}
	}
}
