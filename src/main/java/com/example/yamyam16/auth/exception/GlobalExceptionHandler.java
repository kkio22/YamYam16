package com.example.yamyam16.auth.exception;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.yamyam16.auth.dto.response.UserExceptionResponseDto;

//임시 전역 예외 처리, 추후에 병합할 예정
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<UserExceptionResponseDto> handleUserException(UserException ex) {
		// UserException 에 code 가 null 일 경우, 500 에러
		HttpStatus httpStatus = Optional.ofNullable(HttpStatus.resolve(ex.getCode()))
			.orElse(HttpStatus.INTERNAL_SERVER_ERROR);

		// 예외 응답 DTO 생성
		UserExceptionResponseDto response = new UserExceptionResponseDto(ex.getCode(), httpStatus.getReasonPhrase(),
			ex.getMessage());

		return ResponseEntity.status(ex.getCode()).body(response);
	}

	/**
	 * @Valid 유효성 검증 실패 처리
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<UserExceptionResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();

		UserExceptionResponseDto response = new UserExceptionResponseDto(HttpStatus.BAD_REQUEST.value(),
			HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
