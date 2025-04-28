package com.example.yamyam16.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	//menu
	INVALID_INPUT_VALUE(404, "Not Found", "M001", "Invalid Input Value"),
	MENU_NOT_FOUND(404, "Not Found", "M002", "조회된 메뉴가 없습니다."),

	// Store
	STORE_NOT_FOUND(404, "Not Found", "S001", "조회된 가게가 없습니다."),

	// Cart
	CART_NOT_FOUND(404, "Not Found", "C001", "해당 장바구니가 존재하지 않습니다."),
	DIFFERENT_STORE_CART(400, "Bad Request", "C002", "같은 가게의 음식만 담을 수 있습니다."),
	UNAUTHORIZED_CART_MODIFICATION(403, "Forbidden", "C003", "본인의 장바구니만 수정할 수 있습니다."),
	UNAUTHORIZED_CART_DELETE(403, "Forbidden", "C004", "본인의 장바구니만 삭제할 수 있습니다."),

	// Order
	EMPTY_CART_ORDER(400, "Bad Request", "O001", "장바구니가 비어있어 주문할 수 없습니다."),
	UNAUTHORIZED_ORDER_ACCEPT(403, "Forbidden", "O002", "해당 가게의 주인만 주문을 수락할 수 있습니다."),
	ORDER_NOT_FOUND(404, "Not Found", "O003", "해당 주문이 존재하지 않습니다."),
	UNAUTHORIZED_ORDER_CANCEL(403, "Forbidden", "O004", "본인의 주문만 취소할 수 있습니다."),
	ALREADY_CANCELED_ORDER(400, "Bad Request", "O005", "이미 취소된 주문입니다."),
	ORDER_ALREADY_ACCEPTED(400, "Bad Request", "O006", "이미 주문이 수락되어 취소할 수 없습니다."),
	UNAUTHORIZED_ORDER_ACCESS(403, "Forbidden", "O007", "본인의 주문내역만 조회할 수 있습니다."),
	ORDER_NOT_FOUND_IN_STORE(404, "Not Found", "O008", "해당 가게의 주문이 없습니다."),
	NO_ORDER_HISTORY(404, "Not Found", "O009", "주문 내역이 없습니다.");

	private final int status;
	private final String error;
	private final String code;
	private final String message;
}
