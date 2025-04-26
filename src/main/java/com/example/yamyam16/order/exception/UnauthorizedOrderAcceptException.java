package com.example.yamyam16.order.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class UnauthorizedOrderAcceptException extends CustomException {
	public UnauthorizedOrderAcceptException() {
		super(ErrorCode.UNAUTHORIZED_ORDER_ACCEPT);
	}
}