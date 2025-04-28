package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;
import lombok.Getter;

@Getter
public class SearchStoreResponseDto {

    private Long id;

    private String name;
    private Double grade;
    private Long openTime;
    private Long closeTime;


    public SearchStoreResponseDto(Store store) {

        this.id = store.getId();
        this.name = store.getName();
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
