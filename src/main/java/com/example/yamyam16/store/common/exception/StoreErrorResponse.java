package com.example.yamyam16.store.common.exception;

import org.springframework.http.HttpStatus;

//공통 에러 형식
public interface StoreErrorResponse {

    HttpStatus getHttpStatus();

    String getMessage();
}
