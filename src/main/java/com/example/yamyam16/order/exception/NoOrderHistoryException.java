package com.example.yamyam16.order.exception;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;

public class NoOrderHistoryException extends CustomException {
	public NoOrderHistoryException() {
		super(ErrorCode.NO_ORDER_HISTORY);
	}
}