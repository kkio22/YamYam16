package com.example.yamyam16.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoreRequestDto {

    private String storename;
    private Long open_time;
    private Long close_time;
    private int minOrderPrice;
    private String category;
    private String notice;

}
