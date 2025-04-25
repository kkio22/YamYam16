package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;
import lombok.Getter;

@Getter
public class SearchStoreResponseDto {

    private Long id;

    private String storename;
    private int grade;
    private Long opentime;
    private Long closetime;


    public SearchStoreResponseDto(Store store) {

        this.id = store.getId();
        this.storename = store.getName();
        this.grade = store.getReviews().stream()
                .mapToInt(R::getRating)   // rating이 int라면
                .average()
                .map(avg -> (int) Math.round(avg))
                .orElse(0);
        this.closetime = store.getClosetime();
        this.opentime = store.getOpentime();
    }
}
