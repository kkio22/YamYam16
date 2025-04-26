package com.example.yamyam16.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.yamyam16.order.dto.request.ChangeOrderStatusRequestDto;
import com.example.yamyam16.order.dto.response.ChangeOrderStatusResponseDto;
import com.example.yamyam16.order.dto.response.FindAllOrderResponseDto;
import com.example.yamyam16.order.dto.response.OwnerOrderResponseDto;
import com.example.yamyam16.order.dto.response.UserOrderResponseDto;

@Service
public interface OrderService {

	UserOrderResponseDto save(Long userId);

	OwnerOrderResponseDto accept(Long userId, Long orderId);

	ChangeOrderStatusResponseDto changeStatus(Long userId, Long orderId, ChangeOrderStatusRequestDto statusRequestDto);

	void cancelOrder(Long orderId, Long loginUserId);

	List<FindAllOrderResponseDto> findAll(Long userId);

	OwnerOrderResponseDto findOne(Long userId, Long orderId);
}
