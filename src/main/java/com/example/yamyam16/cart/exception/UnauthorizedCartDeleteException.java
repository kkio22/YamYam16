package com.example.yamyam16.cart.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class UnauthorizedCartDeleteException extends CustomException {
	public UnauthorizedCartDeleteException() {
		super(ErrorCode.UNAUTHORIZED_CART_DELETE);
	}
}