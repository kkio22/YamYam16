package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateStoreResponseDto {
    private final Long id;
    private final String name;
    private final Long openTime;
    private final Long closeTime;
    private final Long minOrderPrice;
    private final String category;
    @Size(max = 100, message = "공지는 30글자 이하로 입력해주세요.")
    private String notice;

    public UpdateStoreResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minOrderPrice = store.getMinOrderPrice();
        this.category = store.getCategory().name();
        this.notice = store.getNotice();
    }
}
