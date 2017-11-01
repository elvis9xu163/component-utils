package com.xjd.utils.component.retry;

/**
 * @author elvis.xu
 * @since 2017-10-31 18:57
 */
public class RetryException extends RuntimeException {
	public RetryException() {
	}

	public RetryException(String message) {
		super(message);
	}

	public RetryException(String message, Throwable cause) {
		super(message, cause);
	}

	public RetryException(Throwable cause) {
		super(cause);
	}

	public RetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public static class RetryInterruptedException extends RetryException {
		public RetryInterruptedException() {
		}

		public RetryInterruptedException(String message) {
			super(message);
		}

		public RetryInterruptedException(String message, Throwable cause) {
			super(message, cause);
		}

		public RetryInterruptedException(Throwable cause) {
			super(cause);
		}

		public RetryInterruptedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}

	public static class RetryExecutionException extends RetryException {
		public RetryExecutionException() {
		}

		public RetryExecutionException(String message) {
			super(message);
		}

		public RetryExecutionException(String message, Throwable cause) {
			super(message, cause);
		}

		public RetryExecutionException(Throwable cause) {
			super(cause);
		}

		public RetryExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}

	public static class RetryTimeoutException extends RetryException {
		public RetryTimeoutException() {
		}

		public RetryTimeoutException(String message) {
			super(message);
		}

		public RetryTimeoutException(String message, Throwable cause) {
			super(message, cause);
		}

		public RetryTimeoutException(Throwable cause) {
			super(cause);
		}

		public RetryTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}

}
