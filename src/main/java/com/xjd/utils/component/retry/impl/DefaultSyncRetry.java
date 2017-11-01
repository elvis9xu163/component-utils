package com.xjd.utils.component.retry.impl;

import com.xjd.utils.basic.AssertUtils;
import com.xjd.utils.component.retry.RetryExecutor;
import com.xjd.utils.component.retry.SyncRetry;

/**
 * @author elvis.xu
 * @since 2017-11-01 09:39
 */
public class DefaultSyncRetry<T> implements SyncRetry<T> {
	protected RetryExecutor<T, T> executor;

	public DefaultSyncRetry(RetryExecutor<T, T> executor) {
		AssertUtils.assertArgumentNonNull(executor, "executor cannot be null.");
		this.executor = executor;
	}

	@Override
	public RetryExecutor<T, T> getSyncExecutor() {
		return executor;
	}
}
