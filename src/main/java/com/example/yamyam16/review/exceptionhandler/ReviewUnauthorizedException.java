package com.example.yamyam16.review.exceptionhandler;

public class ReviewUnauthorizedException extends RuntimeException {
	public ReviewUnauthorizedException(String message) {
		super(message);
	}
}
