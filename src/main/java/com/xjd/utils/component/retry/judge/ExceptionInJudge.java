package com.xjd.utils.component.retry.judge;

import java.util.concurrent.Callable;

import com.xjd.utils.component.retry.RetryJudge;

/**
 * 如果有异常且异常在给定范围，则不断重试；即如果没有异常或异常不在给定范围，则无需重试。
 * @author elvis.xu
 * @since 2017-11-01 09:49
 */
public class ExceptionInJudge<T> implements RetryJudge<T> {
	protected Class<? extends Throwable>[] throwableClasses;

	public ExceptionInJudge(Class<? extends Throwable>... throwableClasses) {
		if (throwableClasses == null || throwableClasses.length == 0) {
			throw new IllegalArgumentException("throwableClasses cannot be empty");
		}
		this.throwableClasses = throwableClasses;
	}

	@Override
	public boolean judge(Callable<T> task, int executedTimes, T result, Throwable throwable) {
		if (throwable != null) {
			for (Class referClass : throwableClasses) {
				if (referClass.isAssignableFrom(throwable.getClass())) {
					return true;
				}
			}
		}
		return false;
	}
}
