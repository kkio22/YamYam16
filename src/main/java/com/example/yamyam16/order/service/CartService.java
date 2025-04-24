package com.example.yamyam16.order.service;

import org.springframework.stereotype.Service;

import com.example.yamyam16.order.dto.request.CartRequestDto;
import com.example.yamyam16.order.dto.request.UpdateCartRequestDto;
import com.example.yamyam16.order.dto.response.FindAllCartResponseDto;
import com.example.yamyam16.order.dto.response.SaveCartResponseDto;

@Service
public interface CartService {

	SaveCartResponseDto save(Long storeId, Long userId, CartRequestDto requestDto);

	FindAllCartResponseDto findAll(Long userId, Long storeId);

	FindAllCartResponseDto update(Long userId, Long cartId, UpdateCartRequestDto requestDto);

	FindAllCartResponseDto delete();
}
