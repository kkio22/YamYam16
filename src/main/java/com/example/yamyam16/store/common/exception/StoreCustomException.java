package com.example.yamyam16.store.common.exception;

import lombok.Getter;

@Getter
//서버쪽 에러 모든 예외는 서비스에서 나와야 하고 컨트롤러에서 에러 쓰면 안됨
//todo : 검색 필요 @RestControllerAdvice -> 컨트롤러 쪽에서 씀 -> 전역쪽에서 씀
public class StoreCustomException extends RuntimeException {

    private final StoreErrorResponse storeErrorCode;//속성


    public StoreCustomException(StoreCustomErrorCode storeErrorCode) {//예외 발생한 곳에서 있는 enum값이 들어옴
        super(storeErrorCode.getMessage());//부모 클래스에 해당 에러 메세지 보냄
        this.storeErrorCode = storeErrorCode; //자기 속성에 저장
    }


}

