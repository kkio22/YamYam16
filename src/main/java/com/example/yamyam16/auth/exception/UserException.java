package com.example.yamyam16.auth.exception;

import com.example.yamyam16.auth.common.exception.UserErrorCode;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
	private final int code;

	public UserException(UserErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}
}
