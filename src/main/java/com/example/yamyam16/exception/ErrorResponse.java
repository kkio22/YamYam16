package com.example.yamyam16.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder//
@JsonInclude(JsonInclude.Include.NON_NULL)//null 또는 빈 값이면, json 응답에서 해당 필드를 제외 즉 반환하지 않는다는 것이다. null인 데이터 반환하지 않는다.
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String path;


}
