package com.example.yamyam16.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.order.dto.request.CartRequestDto;
import com.example.yamyam16.order.dto.request.UpdateCartRequestDto;
import com.example.yamyam16.order.dto.response.FindAllCartResponseDto;
import com.example.yamyam16.order.dto.response.SaveCartResponseDto;
import com.example.yamyam16.order.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/store/{storeId}/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping
	public ResponseEntity<SaveCartResponseDto> saveCart(
		@PathVariable Long storeId,
		@RequestBody CartRequestDto requestDto,
		HttpServletRequest userRequest
	) {
		// 로그인 정보 불러오기
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		return new ResponseEntity<>(cartService.save(storeId, userId, requestDto), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<FindAllCartResponseDto> findAllCart(
		@PathVariable Long storeId,
		HttpServletRequest userRequest
	) {
		// 로그인 정보 불러오기
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		return new ResponseEntity<>(cartService.findAll(userId, storeId), HttpStatus.OK);
	}

	@PatchMapping("{cartId}")
	public ResponseEntity<FindAllCartResponseDto> updateCart(
		@PathVariable Long cartId,
		@RequestBody UpdateCartRequestDto requestDto,
		HttpServletRequest userRequest
	) {
		// 로그인 정보 불러오기
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		return new ResponseEntity<>(cartService.update(userId, cartId, requestDto), HttpStatus.OK);
	}
}
