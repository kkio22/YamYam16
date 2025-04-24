package com.example.yamyam16.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuUpdateResponseDto {

	private Long id;

	private String menuName;

	private int menuPrice;

}
