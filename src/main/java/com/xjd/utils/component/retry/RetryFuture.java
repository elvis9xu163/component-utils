package com.xjd.utils.component.retry;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author elvis.xu
 * @since 2017-11-01 10:05
 */
public interface RetryFuture<T> extends Future<T> {

	/**
	 * @return 当前执行次数
	 */
	int getExecuteTimes();

	@Override
	T get() throws RetryException;

	@Override
	T get(long timeout, TimeUnit unit) throws RetryException;
}
