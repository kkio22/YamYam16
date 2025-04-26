package com.example.yamyam16.order.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class EmptyCartOrderException extends CustomException {
	public EmptyCartOrderException() {
		super(ErrorCode.EMPTY_CART_ORDER);
	}
}
