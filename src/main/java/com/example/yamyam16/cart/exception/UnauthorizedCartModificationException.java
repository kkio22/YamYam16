package com.example.yamyam16.cart.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class UnauthorizedCartModificationException extends CustomException {
	public UnauthorizedCartModificationException() {
		super(ErrorCode.UNAUTHORIZED_CART_MODIFICATION);
	}
}
