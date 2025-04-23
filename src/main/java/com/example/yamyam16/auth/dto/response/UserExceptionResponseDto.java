package com.example.yamyam16.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"code", "status", "message"})
@AllArgsConstructor
public class UserExceptionResponseDto {
	private final int code; // (ex. 200, 201, 401..)
	private final String status; // (ex. OK, CREATED, NOT_FOUND...)
	private final String message; // ex. 생성이 완료되었습니다.
}
