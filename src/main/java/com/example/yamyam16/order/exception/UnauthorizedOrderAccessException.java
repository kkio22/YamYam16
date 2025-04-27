package com.example.yamyam16.order.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class UnauthorizedOrderAccessException extends CustomException {
	public UnauthorizedOrderAccessException() {
		super(ErrorCode.UNAUTHORIZED_ORDER_ACCESS);
	}
}