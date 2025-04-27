package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;

import java.time.LocalTime;

public class CreateStoreResponseDto {

    private Long id;
    private String storeName;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Long minOrderPrice;
    private String category;
    private String notice;

    private CreateStoreResponseDto(Store store) {
        this.id = store.getId();
        this.storeName = store.getName();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minOrderPrice = store.getMinOrderPrice();
        this.category = store.getCategory();
        this.notice = store.getNotice();
    }


    public static CreateStoreResponseDto fromStoreToDto(Store store) {
        return new CreateStoreResponseDto(store);
    }
}
