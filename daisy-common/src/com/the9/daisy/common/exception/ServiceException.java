package com.the9.daisy.common.exception;

/**
 * 
 * @author dingshengheng
 * 
 */
public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6216853027029876313L;

	private int errorCode;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable e) {
		super(e);
	}

	public ServiceException(String message, Throwable e) {
		super(message, e);
	}

	public ServiceException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public ServiceException(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
