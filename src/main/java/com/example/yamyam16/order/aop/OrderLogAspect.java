package com.example.yamyam16.order.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.yamyam16.order.dto.response.ChangeOrderStatusResponseDto;
import com.example.yamyam16.order.dto.response.UserOrderResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class OrderLogAspect {

	@AfterReturning(value = "execution(* com.example.yamyam16.order.service.OrderService.save(..))", returning = "response")
	public void logOrderCreation(JoinPoint joinPoint, Object response) {
		if (response instanceof UserOrderResponseDto dto) {
			log.info("[주문 생성] 시간: {}, 가게: {}, 주문ID: {}",
				LocalDateTime.now(), dto.getStoreName(), dto.getOrderId());
		}
	}

	@AfterReturning(value = "execution(* com.example.yamyam16.order.service.OrderService.changeStatus(..))", returning = "response")
	public void logOrderStatusChange(JoinPoint joinPoint, Object response) {
		if (response instanceof ChangeOrderStatusResponseDto dto) {
			log.info("[주문 상태 변경] 시간: {}, 주문ID: {}, 현재상태: {}",
				LocalDateTime.now(), dto.getOrderId(), dto.getStatus());
		}
	}

}
