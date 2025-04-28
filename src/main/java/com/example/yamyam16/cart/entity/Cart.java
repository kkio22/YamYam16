package com.example.yamyam16.cart.entity;

import java.time.LocalDateTime;

import com.example.yamyam16.cart.enums.CartStatus;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.order.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order orders;

	private Long quantity;

	@Setter
	@Enumerated(EnumType.STRING)
	private CartStatus status;

	private LocalDateTime createdAt;

	// 카트 담기용 생성자
	public Cart(Long userId, Menu menu, Long quantity) {
		this.userId = userId;
		this.menu = menu;
		this.quantity = quantity;
		this.status = CartStatus.IN_CART;
	}

	// 카트 수량 수정
	public void update(Long quantity) {
		this.quantity = quantity;
	}
}
