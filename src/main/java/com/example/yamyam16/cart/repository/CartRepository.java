package com.example.yamyam16.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.cart.entity.Cart;
import com.example.yamyam16.cart.enums.CartStatus;
import com.example.yamyam16.cart.exception.CartNotFoundException;

public interface CartRepository extends JpaRepository<Cart, Long> {

	default Cart findByIdOrElseThrow(Long cartId) {
		return findById(cartId)
			.orElseThrow(CartNotFoundException::new);
	}

	List<Cart> findByUserIdAndStatus(Long userId, CartStatus cartStatus);

	List<Cart> findByOrders_OrderId(Long orderId);
}
