package com.example.yamyam16.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.cart.entity.Cart;
import com.example.yamyam16.cart.enums.CartStatus;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Cart findByCartId(Long cartId);

	List<Cart> findByUserIdAndStatus(Long userId, CartStatus cartStatus);
}
