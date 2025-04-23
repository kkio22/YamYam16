package com.example.yamyam16.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
		HttpServletRequest httpServletRequest) {
		log.error("MethodArgumentNotValidException: {}", e.getMessage());//내부 디버깅용 메세지 출력
		ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE; //ErrorCode는 static이어서 그 클래스명.매서드명으로 가져와서 그걸 이 globalExceptionHandler에 있는 errorCode에 넣음
		String errorMessage = e.getBindingResult().getFieldErrors() != null ?
			e.getBindingResult().getFieldError().getDefaultMessage() : errorCode.getMessage();
		ErrorResponse errorResponse = ErrorResponse.builder()//new + 생성자 대체용
			.status(errorCode.getStatus())
			.error(errorCode.getError())
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.path(httpServletRequest.getRequestURI())
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));

	}

	@ExceptionHandler(CustomException.class) // 이제 여기서 로직 수행
	public ResponseEntity<ErrorResponse> handleStoreNotFoundException(CustomException e,
		HttpServletRequest httpServletRequest) {
		log.error("CustomException: {}", e.getMessage());
		ErrorCode errorCode = e.getErrorCode(); //customException에 저장된 친구 가지고 나오는 것
		ErrorResponse errorResponse = ErrorResponse.builder()
			.error(errorCode.getError())
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.status(errorCode.getStatus())
			.path(httpServletRequest.getRequestURI())
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));

	}
}
