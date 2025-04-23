package com.example.yamyam16.domain.menu.dto;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuCreateRequestDto { //requestBody로 오는 내용만 적음

	@NotBlank
	private String menuName;

	@NotNull
	private int menuPrice; //Long은 객체여서 안 되나?
}
