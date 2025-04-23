package com.example.yamyam16.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.order.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	List<Cart> user(User user);
}
