package com.example.yamyam16.review.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.example.yamyam16.review.dto.ReviewRequestDto;
import com.example.yamyam16.review.dto.ReviewResponseDto;

import jakarta.servlet.http.HttpSession;

public interface ReviewService {

	void updateReview(Long reviewId, ReviewRequestDto dto, HttpSession session);

	void deleteReview(Long reviewId, HttpSession session);

	void createReview(Long storeId, Long orderId, ReviewRequestDto reviewRequestDto,
		HttpSession session);

	@Transactional(readOnly = true)
	Page<ReviewResponseDto> getReviewByStore(Long storeId,
		Integer minGrading,
		Integer maxGrading,
		org.springframework.data.domain.Pageable pageable);
}
