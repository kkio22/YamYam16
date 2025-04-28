package com.example.yamyam16.exception;

import com.example.yamyam16.auth.dto.response.UserExceptionResponseDto;
import com.example.yamyam16.auth.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
    // 	HttpServletRequest httpServletRequest) {
    // 	log.error("MethodArgumentNotValidException: {}", e.getMessage());//내부 디버깅용 메세지 출력
    // 	ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE; //ErrorCode는 static이어서 그 클래스명.매서드명으로 가져와서 그걸 이 globalExceptionHandler에 있는 errorCode에 넣음
    // 	String errorMessage = e.getBindingResult().getFieldErrors() != null ?
    // 		e.getBindingResult().getFieldError().getDefaultMessage() : errorCode.getMessage();
    // 	ErrorResponse errorResponse = ErrorResponse.builder()//new + 생성자 대체용
    // 		.status(errorCode.getStatus())
    // 		.error(errorCode.getError())
    // 		.code(errorCode.getCode())
    // 		.message(errorCode.getMessage())
    // 		.path(httpServletRequest.getRequestURI())
    // 		.build();
    // 	return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
    //
    // }

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
