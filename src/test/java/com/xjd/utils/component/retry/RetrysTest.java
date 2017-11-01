package com.xjd.utils.component.retry;

import org.junit.Test;

/**
 * @author elvis.xu
 * @since 2017-11-01 18:00
 */
public class RetrysTest {
	@Test
	public void syncRetry() throws Exception {

		{
			String s = Retrys.syncRetry(() -> "XXX", Retrys.Judges.hasExceptionAndInLimitTimes(3), Retrys.Executors.syncDirect());
			System.out.println(s);
		}
		{
			String s = Retrys.syncRetry(() -> "HHH", Retrys.Judges.hasExceptionAndInLimitTimes(3));
			System.out.println(s);
		}
		{
			try {
				Retrys.syncRetryMostNTimes(()-> {throw new Exception("TTTT");}, 3);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		{
			try {
				Retrys.syncRetry(()-> {throw new IllegalArgumentException("YYY");}, Retrys.Judges.exceptionNotIn(IllegalArgumentException.class).and(Retrys.Judges.inLimitTimes(3)));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

}