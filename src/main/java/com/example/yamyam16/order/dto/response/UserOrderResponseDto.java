package com.example.yamyam16.order.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserOrderResponseDto {

	private final Long orderId;
	private final String store;
	private final Long totalPrice;
	private String orderStatus;
	private LocalDateTime orderedAt;

}
