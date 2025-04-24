package com.example.yamyam16.menu.dto;

import com.example.yamyam16.MenuStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuCreateResponseDto {

	private Long id;

	private String menuName;

	private long menuPrice;

	private MenuStatus menuStatus;
}
