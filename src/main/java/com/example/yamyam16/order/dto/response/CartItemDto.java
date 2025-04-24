package com.example.yamyam16.order.dto.response;

import com.example.yamyam16.order.entity.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemDto {

	private Long cartId;
	private String menu;
	private Long quantity;
	private Long price;

	public static CartItemDto toDto(Cart cart) {
		Long menuPrice = cart.getMenu().getPrice();
		return new CartItemDto(
			cart.getCartId(),
			cart.getMenu().getName(),
			cart.getQuantity(),
			menuPrice * cart.getQuantity()
		);
	}
}
