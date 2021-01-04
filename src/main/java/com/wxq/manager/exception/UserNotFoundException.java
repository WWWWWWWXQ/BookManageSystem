package com.wxq.manager.exception;

public class UserNotFoundException extends Exception {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
		System.out.println("检查过了 没有此用户");
	}
}
