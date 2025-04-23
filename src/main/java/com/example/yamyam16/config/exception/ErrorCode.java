package com.example.yamyam16.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	//menu
	INVALID_INPUT_VALUE(404, "Not Found", "M001", "Invalid Input Value"),
	STORE_NOT_FOUND(404, "Not Found", "M002", "Store Not Found"),
	MENU_NOT_FOUND(404, "Not Found", "M003", "Menu Not Found");

	private final int status;
	private final String error;
	private final String code;
	private final String message;
}
