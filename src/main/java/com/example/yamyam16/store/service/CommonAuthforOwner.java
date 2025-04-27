package com.example.yamyam16.store.service;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.entity.UserType;
import com.example.yamyam16.store.common.exception.StoreCustomErrorCode;
import com.example.yamyam16.store.common.exception.StoreCustomException;
import com.example.yamyam16.store.dto.response.DeactivateStoreResponseDto;

public abstract class CommonAuthforOwner {
    //객체 생성 안되도록 abstract
    protected final void validateOwnerRole(User user) {
        //권환확인
        if (!user.getUserType().equals(UserType.OWNER)) {
            throw new StoreCustomException(StoreCustomErrorCode.AUTH_NOT_FOUND);
        }
    }

    protected final void store(DeactivateStoreResponseDto deactivateStoreResponseDto) {
    }
}
