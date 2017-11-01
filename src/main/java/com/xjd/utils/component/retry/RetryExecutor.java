package com.xjd.utils.component.retry;

import java.util.concurrent.Callable;

/**
 * @author elvis.xu
 * @since 2017-11-01 09:41
 */
public interface RetryExecutor<T, R> {
	R execute(Callable<T> task, int executeTimes, T lastResult, Throwable lastThrowable) throws Exception;
}
