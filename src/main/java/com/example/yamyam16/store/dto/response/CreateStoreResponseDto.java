package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateStoreResponseDto {

    private Long id;
    @NotBlank(message = "상호명을 입력해주세요")
    private String name;
    @NotBlank(message = "개장시간을 입력해주세요")
    private Long openTime;
    @NotBlank(message = "마감시간을 입력해주세요")
    private Long closeTime;
    @NotBlank(message = "최소주문금액을 입력해주세요")
    private Long minOrderPrice;
    @NotBlank(message = "카테고리를 입력해주세요")
    private String category;
    private String notice;

    private CreateStoreResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minOrderPrice = store.getMinOrderPrice();
        this.category = store.getCategory().name();
        this.notice = store.getNotice();
    }

    public static CreateStoreResponseDto fromStoreToDto(Store store) {
        return new CreateStoreResponseDto(store);
    }
}
