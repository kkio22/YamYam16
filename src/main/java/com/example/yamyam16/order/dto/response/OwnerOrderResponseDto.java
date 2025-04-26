package com.example.yamyam16.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OwnerOrderResponseDto {

	private final Long orderId;
	private final List<MenuItemDto> menuItems;
	private final Long totalPrice;
	private final String orderStatus;
	private final LocalDateTime orderedAt;

}
