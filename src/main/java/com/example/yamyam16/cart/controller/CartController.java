package com.example.yamyam16.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.cart.dto.request.CartRequestDto;
import com.example.yamyam16.cart.dto.request.DeleteCartRequestDto;
import com.example.yamyam16.cart.dto.request.UpdateCartRequestDto;
import com.example.yamyam16.cart.dto.response.FindAllCartResponseDto;
import com.example.yamyam16.cart.dto.response.SaveCartResponseDto;
import com.example.yamyam16.cart.service.CartService;

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

	@PatchMapping
	public ResponseEntity<FindAllCartResponseDto> updateCart(
		@RequestBody UpdateCartRequestDto requestDto,
		HttpServletRequest userRequest
	) {
		// 로그인 정보 불러오기
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		return new ResponseEntity<>(cartService.update(userId, requestDto), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<FindAllCartResponseDto> deleteCart(
		@RequestBody DeleteCartRequestDto requestDto,
		HttpServletRequest userRequest
	) {
		// 로그인 정보 불러오기
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		return new ResponseEntity<>(cartService.delete(userId, requestDto), HttpStatus.OK);
	}

	@DeleteMapping("/all")
	public ResponseEntity<String> deleteAllCart(
		HttpServletRequest userRequest
	) {
		// 로그인 정보 불러오기
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		cartService.deleteAll(userId);

		return ResponseEntity.ok(" 장바구니가 초기화되었습니다.");
	}

}
