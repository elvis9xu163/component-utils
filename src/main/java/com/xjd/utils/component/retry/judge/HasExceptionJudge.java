package com.xjd.utils.component.retry.judge;

import java.util.concurrent.Callable;

import com.xjd.utils.component.retry.RetryJudge;

/**
 * 有异常则重试
 * @author elvis.xu
 * @since 2017-11-01 09:49
 */
public class HasExceptionJudge<T> implements RetryJudge<T> {
	@Override
	public boolean judge(Callable<T> task, int executedTimes, T result, Throwable throwable) {
		return throwable != null;
	}
}
