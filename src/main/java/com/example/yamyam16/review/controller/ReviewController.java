// package com.example.yamyam16.review.controller;
//
// import java.awt.print.Pageable;
//
// import org.springframework.data.domain.Page;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.example.yamyam16.review.dto.ReviewRequestDto;
// import com.example.yamyam16.review.dto.ReviewResponseDto;
// import com.example.yamyam16.review.service.ReviewService;
//
// import jakarta.servlet.http.HttpSession;
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequiredArgsConstructor
// public class ReviewController {
//
// 	private final ReviewService reviewService;
//
// 	@PostMapping("/store/{storeId}/review")
// 	public ResponseEntity<Void> createReview(@RequestBody ReviewRequestDto reviewRequestDto, HttpSession session) {
// 		reviewService.createReview(reviewRequestDto, session);
// 		return ResponseEntity.ok().build();
// 	}
//
// 	@GetMapping("/store/{storeId}/review")
// 	public ResponseEntity<Page<ReviewResponseDto>> getReviews(@PathVariable Long storeId,
// 		@RequestParam(required = false) Integer minGrating, @RequestParam(required = false) Integer maxGrating,
// 		Pageable pageable) {
// 		return ResponseEntity.ok(reviewService.getReviewByStore(storeId, minGrating, maxGrating, pageable));
// 	}
//
// 	@PutMapping("/store/{storeId}/review/{reviewid}")
// 	public ResponseEntity<Void> updateReview(@PathVariable Long reviewId,
// 		@RequestBody ReviewRequestDto reviewRequestDto, HttpSession session) {
// 		reviewService.updateReview(reviewId, reviewRequestDto, session);
// 		return ResponseEntity.ok().build();
// 	}
//
// 	@DeleteMapping("/store/{storeId}/review/{reviewid}")
// 	public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId, HttpSession session) {
// 		reviewService.deleteReview(reviewId, session);
// 		return ResponseEntity.noContent().build();
// 	}
//
// }
