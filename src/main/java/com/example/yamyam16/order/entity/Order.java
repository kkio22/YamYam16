package com.example.yamyam16.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.yamyam16.cart.entity.Cart;
import com.example.yamyam16.order.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	private Long userId;

	private Long storeId;

	private LocalDateTime orderedAt;

	private Long totalPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
	private List<Cart> carts = new ArrayList<>();

	// 유저 - 주문생성용
	public Order(Long userId, Long storeId, Long totalPrice, OrderStatus status, LocalDateTime orderedAt) {
		this.userId = userId;
		this.storeId = storeId;
		this.totalPrice = totalPrice; //서비스단에서 꼭 직접계산하기
		this.status = status;
		this.orderedAt = orderedAt;
	}

}
