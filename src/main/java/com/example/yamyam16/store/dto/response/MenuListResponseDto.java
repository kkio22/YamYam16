package com.example.yamyam16.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuListResponseDto {

    private Long id;

    private String menuName;

    private int menuPrice;

}
