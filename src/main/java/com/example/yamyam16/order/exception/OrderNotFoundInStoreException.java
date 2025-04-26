package com.example.yamyam16.order.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class OrderNotFoundInStoreException extends CustomException {
	public OrderNotFoundInStoreException() {
		super(ErrorCode.ORDER_NOT_FOUND_IN_STORE);
	}
}
