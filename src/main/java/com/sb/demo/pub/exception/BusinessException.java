package com.sb.demo.pub.exception;

/**
 * 业务类型异常
 * 
 * @author xg
 * @date 2019-10-23
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -1370815301855467357L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	protected BusinessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
