package com.xjd.utils.component.retry.impl;

import com.xjd.utils.basic.AssertUtils;
import com.xjd.utils.component.retry.AsyncRetry;
import com.xjd.utils.component.retry.RetryExecutor;
import com.xjd.utils.component.retry.RetryFuture;

/**
 * @author elvis.xu
 * @since 2017-11-01 09:39
 */
public class DefaultAsyncRetry<T> implements AsyncRetry<T> {
	protected RetryExecutor<T, RetryFuture<T>> executor;

	public DefaultAsyncRetry(RetryExecutor<T, RetryFuture<T>> executor) {
		AssertUtils.assertArgumentNonNull(executor, "executor cannot be null.");
		this.executor = executor;
	}

	@Override
	public RetryExecutor<T, RetryFuture<T>> getAsyncExecutor() {
		return executor;
	}
}
