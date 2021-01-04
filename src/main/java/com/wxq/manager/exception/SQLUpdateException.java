package com.wxq.manager.exception;

public class SQLUpdateException extends Exception{
	/**
	 * 接收给出的异常信息
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public SQLUpdateException(String message) {
	this.message=message;
}
	public String getMessage() {
	return message;
}
}
