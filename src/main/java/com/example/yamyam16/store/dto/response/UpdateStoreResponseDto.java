package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class UpdateStoreResponseDto {
    private Long id;
    private String storeName;
    private LocalTime opentime;
    private LocalTime closetime;
    private Long minOrderPrice;
    private String category;
    @Size(max = 100, message = "공지는 30글자 이하로 입력해주세요.")
    private String notice;

    public UpdateStoreResponseDto(Store store) {
        this.id = store.getId();
        this.storeName = store.getName();
        this.opentime = store.getOpenTime();
        this.closetime = store.getCloseTime();
        this.minOrderPrice = store.getMinOrderPrice();
        this.category = store.getCategory().name();
        this.notice = store.getNotice();
    }
}
