package com.example.yamyam16.order.service;

import org.springframework.stereotype.Service;

import com.example.yamyam16.order.dto.request.CartRequestDto;
import com.example.yamyam16.order.dto.response.SaveCartResponseDto;

@Service
public interface CartService {

	SaveCartResponseDto save(Long storeId, Long userId, CartRequestDto requestDto);
}
