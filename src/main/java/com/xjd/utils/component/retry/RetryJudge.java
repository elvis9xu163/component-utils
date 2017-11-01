package com.xjd.utils.component.retry;

import java.util.concurrent.Callable;

import com.xjd.utils.basic.AssertUtils;

/**
 * @author elvis.xu
 * @since 2017-11-01 09:42
 */
public interface RetryJudge<T> {
	boolean judge(Callable<T> task, int executedTimes, T result, Throwable throwable);

	default RetryJudge<T> and(RetryJudge<T> judge) {
		AssertUtils.assertArgumentNonNull(judge);
		return (task, executedTimes, result, throwable) -> judge(task, executedTimes, result, throwable) && judge.judge(task, executedTimes, result, throwable);
	}

	default RetryJudge<T> or(RetryJudge<T> judge) {
		AssertUtils.assertArgumentNonNull(judge);
		return (task, executedTimes, result, throwable) -> judge(task, executedTimes, result, throwable) || judge.judge(task, executedTimes, result, throwable);
	}

	public static <T> RetryJudge<T> not(RetryJudge<T> judge) {
		AssertUtils.assertArgumentNonNull(judge);
		return (task, executedTimes, result, throwable) -> !judge.judge(task, executedTimes, result, throwable);
	}
}
