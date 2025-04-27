package com.example.yamyam16.cart.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class UpdateCartRequestDto {

	@Min(value = 0) // 0 입력시 카트 삭제
	private Long quantity;
}

