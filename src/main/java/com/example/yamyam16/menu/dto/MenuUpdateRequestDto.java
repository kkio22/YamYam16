package com.example.yamyam16.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuUpdateRequestDto {

	private String menuName;

	private int menuPrice;
}
