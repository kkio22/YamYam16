package com.example.yamyam16.order.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeOrderStatusResponseDto {

	private final Long orderId;
	private final String status;
	private LocalDateTime orderedAt;
}
