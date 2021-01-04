package com.wxq.web.exception;

/**
 * 未找到制定的书本信息
 */
public class BookNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	private String message;
	public BookNotFoundException(String message){
		this.message=message;
	}
	public String getMessage() {
		return message;
	}
}
