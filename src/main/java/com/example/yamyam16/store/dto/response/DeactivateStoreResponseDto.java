package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;
import lombok.Getter;

@Getter
public class DeactivateStoreResponseDto {

    private final boolean isDeleted;

    public DeactivateStoreResponseDto(Store store) {
        this.isDeleted = store.isDelete();
    }

}
