package com.example.yamyam16.order.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class AlreadyCanceledOrderException extends CustomException {
	public AlreadyCanceledOrderException() {
		super(ErrorCode.ALREADY_CANCELED_ORDER);
	}
}