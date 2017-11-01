package com.xjd.utils.component.retry;

import java.util.concurrent.Callable;

/**
 * @author elvis.xu
 * @since 2017-11-01 09:18
 */
public interface Retry<T, R> {
	R retry(Callable<T> task, RetryJudge<T> judge);
}
