package com.example.yamyam16.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
