package com.example.yamyam16.order.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAllOrderResponseDto {
	private final Long orderId;
	private final String storeName;
	private final String status;
	private LocalDateTime orderedAt;

	// 일반유저가 조회할때

	// 오너가 조회할때

}
