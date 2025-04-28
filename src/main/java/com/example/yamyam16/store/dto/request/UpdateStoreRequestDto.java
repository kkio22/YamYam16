package com.example.yamyam16.store.dto.request;

import com.example.yamyam16.store.entity.CategoryType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateStoreRequestDto {

    private final String name;
    private final Long openTime;
    private final Long closeTime;
    private final Long minOrderPrice;
    private final CategoryType category;
    @Size(max = 100, message = "공지는 30글자 이하로 입력해주세요.")
    private final String notice;

}
