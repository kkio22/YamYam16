package com.example.yamyam16.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreRequestDto {

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

}
