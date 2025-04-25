package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;
import jakarta.validation.constraints.NotBlank;

public class CreateStoreResponseDto {

    private Long id;
    @NotBlank(message = "상호명을 입력해주세요")
    private String name;
    @NotBlank(message = "개장시간을 입력해주세요")
    private Long open_time;
    @NotBlank(message = "마감시간을 입력해주세요")
    private Long close_time;
    @NotBlank(message = "최소주문금액을 입력해주세요")
    private Long minOrderPrice;
    @NotBlank(message = "카테고리를 입력해주세요")
    private String category;
    private String notice;

    public CreateStoreResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.open_time = store.getOpentime();
        this.close_time = store.getClosetime();
        this.minOrderPrice = store.getMinprice();
        this.category = store.getCategory();
        this.notice = store.getNotice();
    }

}
