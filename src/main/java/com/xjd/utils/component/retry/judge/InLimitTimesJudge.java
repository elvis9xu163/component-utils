package com.xjd.utils.component.retry.judge;

import java.util.concurrent.Callable;

import com.xjd.utils.basic.AssertUtils;
import com.xjd.utils.component.retry.RetryJudge;

/**
 * 在给定执行次数范围内则重试；即大于等于给定次数时不再重试。
 * @author elvis.xu
 * @since 2017-11-01 09:49
 */
public class InLimitTimesJudge<T> implements RetryJudge<T> {
	protected int limitTimes;

	public InLimitTimesJudge(int limitTimes) {
		AssertUtils.assertArgumentGreaterEqualThan(limitTimes, 0, "limitTimes must >= 0");
		this.limitTimes = limitTimes;
	}

	@Override
	public boolean judge(Callable<T> task, int executedTimes, T result, Throwable throwable) {
		return executedTimes < limitTimes;
	}
}
