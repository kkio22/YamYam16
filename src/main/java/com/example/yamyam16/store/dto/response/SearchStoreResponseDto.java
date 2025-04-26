package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;
import lombok.Getter;

@Getter
public class SearchStoreResponseDto {

    private Long id;

    private String storename;
    private Long grade;
    private Long opentime;
    private Long closetime;


    public SearchStoreResponseDto(Store store) {

        this.id = store.getId();
        this.storename = store.getName();
//        this.grade = (long) store.getReviews().stream()
//                .mapToLong(review -> review.getGrade())
//                .average()
//                .orElse(0.0);
        this.grade = store.
                this.closetime = store.getCloseTime();
        this.opentime = store.getOpenTime();
        this.closetime = store.getCloseTime();
    }
}
