package com.example.yamyam16.order.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class UnauthorizedOrderCancelException extends CustomException {
	public UnauthorizedOrderCancelException() {
		super(ErrorCode.UNAUTHORIZED_ORDER_CANCEL);
	}
}