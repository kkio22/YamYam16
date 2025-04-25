package com.example.yamyam16.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.order.dto.request.ChangeOrderStatusRequestDto;
import com.example.yamyam16.order.dto.response.ChangeOrderStatusResponseDto;
import com.example.yamyam16.order.dto.response.OwnerOrderResponseDto;
import com.example.yamyam16.order.dto.response.UserOrderResponseDto;
import com.example.yamyam16.order.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/cart/{cartId}/orders")
	public ResponseEntity<UserOrderResponseDto> saveOrder(
		HttpServletRequest userRequest
	) {
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		return new ResponseEntity<>(orderService.save(userId), HttpStatus.CREATED);
	}

	@PostMapping("/{orderId}")
	public ResponseEntity<OwnerOrderResponseDto> acceptOrder(
		@PathVariable Long orderId,
		HttpServletRequest userRequest
	) {
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		return new ResponseEntity<>(orderService.accept(userId, orderId), HttpStatus.OK);
	}

	@PatchMapping("/{orderId}")
	public ResponseEntity<ChangeOrderStatusResponseDto> changeStatus(
		@PathVariable Long orderId,
		@RequestBody ChangeOrderStatusRequestDto statusRequestDto,
		HttpServletRequest userRequest
	) {
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		return new ResponseEntity<>(orderService.changeStatus(userId, orderId, statusRequestDto), HttpStatus.OK);
	}

	@DeleteMapping({"/{orderId}"})
	public ResponseEntity<String> cancleOrder(
		@PathVariable Long orderId,
		@RequestBody ChangeOrderStatusRequestDto statusRequestDto,
		HttpServletRequest userRequest
	) {
		HttpSession session = userRequest.getSession();
		Long userId = (Long)session.getAttribute("userId");

		orderService.cancleOrder(userId, orderId, statusRequestDto);

		return ResponseEntity.ok("주문 취소가 완료되었습니다.");
	}

}
