package com.xjd.utils.component.retry.judge;

import java.util.concurrent.Callable;

import com.xjd.utils.basic.AssertUtils;
import com.xjd.utils.component.retry.RetryJudge;

/**
 * 满足以下所有条件则重试:
 * 1.有异常
 * 2.执行次数在给定次数范围内
 * @author elvis.xu
 * @since 2017-11-01 09:49
 */
public class HasExceptionAndInLimitTimesJudge<T> extends HasExceptionJudge<T> implements RetryJudge<T> {
	protected int limitTimes;

	public HasExceptionAndInLimitTimesJudge(int limitTimes) {
		AssertUtils.assertArgumentGreaterEqualThan(limitTimes, 0, "limitTimes must >= 0");
		this.limitTimes = limitTimes;
	}

	@Override
	public boolean judge(Callable<T> task, int executedTimes, T result, Throwable throwable) {
		return executedTimes < limitTimes && super.judge(task, executedTimes, result, throwable);
	}
}
