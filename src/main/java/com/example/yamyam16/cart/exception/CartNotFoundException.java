package com.example.yamyam16.cart.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class CartNotFoundException extends CustomException {
	public CartNotFoundException() {
		super(ErrorCode.CART_NOT_FOUND);
	}
}
