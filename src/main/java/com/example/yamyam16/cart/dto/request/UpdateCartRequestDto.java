package com.example.yamyam16.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCartRequestDto {

	@NotBlank
	private String menuName;

	@Min(value = 0) // 0 입력시 카트 삭제
	private Long quantity;
}

