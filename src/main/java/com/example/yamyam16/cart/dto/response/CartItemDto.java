package com.example.yamyam16.cart.dto.response;

import com.example.yamyam16.cart.entity.Cart;

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
		Long menuPrice = cart.getMenu().getMenuPrice();
		return new CartItemDto(
			cart.getCartId(),
			cart.getMenu().getMenuName(),
			cart.getQuantity(),
			menuPrice * cart.getQuantity()
		);
	}
}
