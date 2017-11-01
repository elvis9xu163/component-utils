package com.xjd.utils.component.retry;

import java.util.concurrent.Callable;

/**
 * @author elvis.xu
 * @since 2017-11-01 09:26
 */
public interface SyncRetry<T> extends Retry<T, T> {

	@Override
	default T retry(Callable<T> task, RetryJudge<T> judge) {
		int times = 0;
		T rt = null;
		Exception th = null;
		do {
			rt = null;
			th = null;
			times++;
			try {
				rt = getSyncExecutor().execute(task, times, rt, th);
			} catch (Exception t) {
				th = t;
			}
		} while (judge.judge(task, times, rt, th));

		if (th != null) {
			throw new RetryException.RetryExecutionException("execute task failed: executedTimes=" + times, th);
		}
		return rt;
	}

	RetryExecutor<T, T> getSyncExecutor();
}
