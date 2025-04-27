package com.example.yamyam16.cart.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class DifferentStoreCartException extends CustomException {
	public DifferentStoreCartException() {
		super(ErrorCode.DIFFERENT_STORE_CART);
	}
}