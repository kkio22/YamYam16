package com.example.yamyam16.order.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class OrderNotFoundException extends CustomException {
	public OrderNotFoundException() {
		super(ErrorCode.ORDER_NOT_FOUND);
	}
}
