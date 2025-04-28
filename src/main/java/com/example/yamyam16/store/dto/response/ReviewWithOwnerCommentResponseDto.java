package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.review.dto.ReviewResponseDto;
import lombok.Getter;

@Getter
public class ReviewWithOwnerCommentResponseDto {

    private ReviewResponseDto review;
    private OwnerCommentResponseDto comment;

    public ReviewWithOwnerCommentResponseDto(ReviewResponseDto review, OwnerCommentResponseDto comment) {
        this.review = review;
        this.comment = comment;
    }

}

