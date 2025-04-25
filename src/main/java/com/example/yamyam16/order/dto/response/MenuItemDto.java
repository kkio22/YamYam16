package com.example.yamyam16.order.dto.response;

import com.example.yamyam16.cart.entity.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuItemDto {

	private String menu;
	private Long quantity;

	public static MenuItemDto toDto(Cart cart) {
		return new MenuItemDto(
			cart.getMenu().getMenuName(),
			cart.getQuantity()
		);
	}
}
