package com.example.yamyam16.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.repository.UserRepository;
import com.example.yamyam16.order.entity.Order;
import com.example.yamyam16.order.enums.OrderStatus;
import com.example.yamyam16.order.repository.OrderRepository;
import com.example.yamyam16.review.dto.ReviewRequestDto;
import com.example.yamyam16.review.dto.ReviewResponseDto;
import com.example.yamyam16.review.entity.Review;
import com.example.yamyam16.review.exceptionhandler.ReviewUnauthorizedException;
import com.example.yamyam16.review.repository.ReviewRepository;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;

	@Override
	public void createReview(Long storeId, Long orderId, ReviewRequestDto reviewRequestDto,
		HttpSession session) {

		Long userId = (Long)session.getAttribute("userId");

		if (userId == null) {
			throw new ReviewUnauthorizedException("로그인이 필요합니다.");
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다"));

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

		if (order.getStatus() != OrderStatus.DELIVERED) {
			throw new IllegalStateException("배달 완료된 주문만 리뷰를 작성할 수 있습니다");
		}

		if (!order.getUserId().equals(user.getId())) {
			throw new IllegalAccessError("본인 주문에 대해서만 리뷰 작성이 가능합니다");
		}

		Store store = storeRepository.findByIdOrElseThrow(order.getStoreId());

		Review review = new Review();
		review.setContent(reviewRequestDto.getContent());
		review.setGrade(reviewRequestDto.getGrade());
		review.setOrder(order);
		review.setStore(store);

		reviewRepository.save(review);

	}

	@Transactional(readOnly = true)
	@Override
	public Page<ReviewResponseDto> getReviewByStore(Long storeId,
		Integer minGrading,
		Integer maxGrading,
		Pageable pageable) {
		int min = (minGrading != null) ? minGrading : 1;
		int max = (maxGrading != null) ? maxGrading : 5;

		Page<Review> reviewPage = reviewRepository
			.findByOrderStoreIdAndGradeBetweenOrderByCreatedAtDesc(storeId, min, max, pageable);

		return reviewPage.map(review -> new ReviewResponseDto(
			review.getContent(),
			review.getGrade(),
			review.getCreatedAt()));
	}

	@Override
	public void updateReview(Long storeId, Long reviewId, ReviewRequestDto dto, HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		if (userId == null) {
			throw new ReviewUnauthorizedException("로그인이 필요합니다");
		}

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다"));

		Order order = review.getOrder();
		if (order == null) {
			throw new IllegalStateException("리뷰에 연결된 주문 정보가 없습니다.");
		}

		if (!review.getOrder().getUserId().equals(userId)) {
			throw new IllegalAccessError("본인의 리뷰만 수정할 수 있습니다");
		}

		review.setContent(dto.getContent());
		review.setGrade(dto.getGrade());

		reviewRepository.save(review);

	}

	@Override
	public void deleteReview(Long storeId, Long reviewId, HttpSession session) {
		Long userId = (Long)session.getAttribute("userId");
		if (userId == null) {
			throw new ReviewUnauthorizedException("로그인이 필요합니다");
		}
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다"));

		if (!review.getOrder().getUserId().equals(userId)) {
			throw new IllegalAccessError("본인의 리뷰만 삭제 가능합니다");
		}

		reviewRepository.delete(review);
	}
}
