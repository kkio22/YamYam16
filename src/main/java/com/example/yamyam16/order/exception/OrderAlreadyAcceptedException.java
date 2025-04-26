package com.example.yamyam16.order.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class OrderAlreadyAcceptedException extends CustomException {
	public OrderAlreadyAcceptedException() {
		super(ErrorCode.ORDER_ALREADY_ACCEPTED);
	}
}