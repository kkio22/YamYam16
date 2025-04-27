package com.example.yamyam16.store.dto.request;

import com.example.yamyam16.store.entity.enums.CategoryType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoreRequestDto {

    private String storeName;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Long minOrderPrice;
    private CategoryType category;
    @Size(max = 100, message = "공지는 30글자 이하로 입력해주세요.")
    private String notice;

}
