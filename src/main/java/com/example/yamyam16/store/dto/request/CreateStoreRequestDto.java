package com.example.yamyam16.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateStoreRequestDto {

    @NotBlank(message = "상호명을 입력해주세요")
    private final String storeName;
    @NotNull(message = "개장시간을 입력해주세요")
    private final LocalTime openTime;
    @NotNull(message = "마감시간을 입력해주세요")
    private final LocalTime closeTime;
    @NotNull(message = "최소주문금액을 입력해주세요")
    private final Long minOrderPrice;
    @NotBlank(message = "카테고리를 입력해주세요")
    private final String category;
    private final String notice;

}
