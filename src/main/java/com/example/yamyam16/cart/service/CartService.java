package com.example.yamyam16.cart.service;

import org.springframework.stereotype.Service;

import com.example.yamyam16.cart.dto.request.CartRequestDto;
import com.example.yamyam16.cart.dto.request.UpdateCartRequestDto;
import com.example.yamyam16.cart.dto.response.FindAllCartResponseDto;
import com.example.yamyam16.cart.dto.response.SaveCartResponseDto;

@Service
public interface CartService {

	SaveCartResponseDto save(Long storeId, Long userId, CartRequestDto requestDto);

	FindAllCartResponseDto findAll(Long userId, Long storeId);

	FindAllCartResponseDto update(Long userId, UpdateCartRequestDto requestDto);

	FindAllCartResponseDto delete(Long userId, Long cartId);

	void deleteAll(Long userId);
}
