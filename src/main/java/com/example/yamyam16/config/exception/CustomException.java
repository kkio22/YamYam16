package com.example.yamyam16.config.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;//속성

	public CustomException(ErrorCode errorcode) {//예외 발생한 곳에서 있는 enum값이 들어옴
		super(errorcode.getMessage());//부모 클래스에 해당 에러 메세지 보냄
		this.errorCode = errorcode; //자기 속성에 저장
	}
}

