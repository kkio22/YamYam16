package com.example.yamyam16.order.dto.request;

import com.example.yamyam16.order.enums.OrderStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderStatusRequestDto {

	@NotBlank(message = "주문 상태를 입력해주세요.\n"
		+ "ORDERED,      // 주문 완료\n"
		+ "PREPARING,    // 음식 준비 중\n"
		+ "DELIVERING,   // 배달 중\n"
		+ "DELIVERED,    // 배달 완료\n"
		+ "CANCELED      // 주문 취소")
	private OrderStatus status;
}
