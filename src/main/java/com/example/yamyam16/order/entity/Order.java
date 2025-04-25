package com.example.yamyam16.order.entity;

import java.time.LocalDateTime;

import com.example.yamyam16.order.enums.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "order")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartId;

	private Long userId;

	private Long storeId;

	private LocalDateTime createdAt;

	private Long totalPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

}
