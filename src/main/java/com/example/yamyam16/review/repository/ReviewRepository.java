package com.example.yamyam16.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.yamyam16.order.entity.Order;
import com.example.yamyam16.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	boolean existsByOrder(Order order);

	// Page<Review> findByOrderStoreIdAndGradeBetweenOrderByCreatedAtDesc(Long storeId, int min, int max,
	// 	Pageable pageable);
}
