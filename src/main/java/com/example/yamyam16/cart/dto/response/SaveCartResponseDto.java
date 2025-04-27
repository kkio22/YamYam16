package com.example.yamyam16.cart.dto.response;

import java.util.List;

import com.example.yamyam16.cart.entity.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveCartResponseDto {

	private String message;
	private CartItemDto addedItem; // 카트 아이템 상세 (방금 추가한 것만)
	private final Long totalCartPrice; // 카트 총 금액 (전체)
	private final Long minOrderPrice; // 최소 주문 금액
	private boolean canOrder; // 주문 가능 여부 -> 최소 주문 금액을 충족하는가?

	public static SaveCartResponseDto toDto(String message, Cart addedCart, List<Cart> userCarts, Long minOrderPrice) {
		// 현재 카트 금액 구하기
		List<CartItemDto> cartItems = userCarts.stream().map(CartItemDto::toDto).toList();
		Long totalCartPrice = cartItems.stream().mapToLong(CartItemDto::getPrice).sum()
			+ addedCart.getQuantity() * addedCart.getMenu().getMenuPrice();

		return new SaveCartResponseDto(
			message,
			CartItemDto.toDto(addedCart),
			totalCartPrice,
			minOrderPrice,
			totalCartPrice >= minOrderPrice
		);
	}
}
