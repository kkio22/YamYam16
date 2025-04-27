package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class SearchStoreResponseDto {

    private Long id;

    private String storeName;
    private Double grade;
    private LocalTime openTime;
    private LocalTime closeTime;


    public SearchStoreResponseDto(Store store) {

        this.id = store.getId();
        this.storeName = store.getName();
        this.grade = store.getReviews().stream()
                .mapToLong(review_grade -> review_grade.getGrade())
                .average()
                .orElse(0.0);
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
    }

    public static SearchStoreResponseDto fromStoreToDto(Store store) {
        return new SearchStoreResponseDto(store);
    }
}
