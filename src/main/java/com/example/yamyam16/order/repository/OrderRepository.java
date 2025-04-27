package com.example.yamyam16.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.order.entity.Order;
import com.example.yamyam16.order.exception.NoOrderHistoryException;
import com.example.yamyam16.order.exception.OrderNotFoundException;
import com.example.yamyam16.order.exception.OrderNotFoundInStoreException;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findAllByStoreId(Long storeId);

	List<Order> findAllByUserId(Long userId);

	default Order findByIdOrElseThrow(Long orderId) {
		return findById(orderId)
			.orElseThrow(OrderNotFoundException::new);
	}

	default List<Order> findByStoreIdOrElseThrow(Long storeId) {
		List<Order> orders = findAllByStoreId(storeId);
		if (orders.isEmpty()) {
			throw new OrderNotFoundInStoreException();
		}
		return orders;
	}

	default List<Order> findAllByUserIdOrElseThrow(Long userId) {
		List<Order> orders = findAllByUserId(userId);
		if (orders.isEmpty()) {
			throw new NoOrderHistoryException();
		}
		return orders;
	}

}
