package com.example.yamyam16.store.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum StoreCustomErrorCode implements StoreErrorResponse {
    //왜 implements 받아서 쓰는지 - > 공통 응답 -> 공통형식따라서 에러 메세지 한다
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "사장님 권한이 없습니다"),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게가 존재하지 않습니다"),
    OVER_CREATE(HttpStatus.BAD_REQUEST, "가게는 최대 3개만 만들 수 있습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "사장님의 댓글이 존재하지 않습니다");


    private final HttpStatus httpStatus;
    private final String message;

}
