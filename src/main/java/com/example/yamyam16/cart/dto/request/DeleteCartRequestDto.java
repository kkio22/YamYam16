package com.example.yamyam16.cart.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteCartRequestDto {

	@NotBlank
	private String menuName;

}