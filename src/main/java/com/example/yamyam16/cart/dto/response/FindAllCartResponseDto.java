package com.example.yamyam16.cart.dto.response;

import java.util.List;

import com.example.yamyam16.cart.entity.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAllCartResponseDto {

	private String message; // 조회시 출력 메세지
	private final List<CartItemDto> currentItems;
	private final Long totalCartPrice; // 카트 총 금액 (전체)
	private final Long minOrderPrice; // 최소 주문 금액
	private boolean canOrder; // 주문 가능 여부 -> 최소 주문 금액을 충족하는가?

	public static FindAllCartResponseDto toDto(String message, List<Cart> userCarts, Long minOrderPrice) {
		List<CartItemDto> cartItems = userCarts.stream().map(CartItemDto::toDto).toList();

		Long totalCartPrice = cartItems.stream().mapToLong(CartItemDto::getPrice).sum();

		return new FindAllCartResponseDto(
			message,
			cartItems,
			totalCartPrice,
			minOrderPrice,
			totalCartPrice >= minOrderPrice
		);
	}
}
