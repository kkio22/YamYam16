package com.example.yamyam16.domain.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.domain.menu.dto.MenuCreateRequestDto;
import com.example.yamyam16.domain.menu.dto.MenuCreateResponseDto;
import com.example.yamyam16.domain.menu.service.MenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MenuController {

	public final MenuService menuService;

	@PostMapping("/stores/{storeId}/menu")
	public ResponseEntity<MenuCreateResponseDto> menuCreate(
		@PathVariable Long storeId, //지금 연관 관계된 가게의 url에 데이터 베이스 id 값이 있기에 그걸로 연관 지으면 됨
		@RequestBody MenuCreateRequestDto menuCreateRequestDto
	) {
		MenuCreateResponseDto menuCreateResponseDto = menuService.menuCreate(storeId, menuCreateRequestDto); //dto
		return new ResponseEntity<>(menuCreateResponseDto, HttpStatus.CREATED);
	}

}
