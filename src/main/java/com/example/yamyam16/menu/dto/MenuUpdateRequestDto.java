package com.example.yamyam16.menu.dto;

import com.example.yamyam16.MenuStatus;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuUpdateRequestDto {

	private String menuName;

	@Positive(message = "메뉴 가격은 0보다 커야 합니다")
	private Long menuPrice;

	private MenuStatus menuStatus;
}
