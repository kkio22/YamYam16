package com.example.yamyam16.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDto {

	@NotBlank(message = "메뉴 이름을 입력해주세요.")
	private String menu;

	@Min(value = 1, message = "메뉴 수량은 1개 이상으로 입력해주세요.")
	private Long quantity;
}