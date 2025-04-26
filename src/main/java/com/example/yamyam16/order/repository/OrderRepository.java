package com.example.yamyam16.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByStoreId(Long storeId);

	List<Order> findAllByUserId(Long userId);

}
