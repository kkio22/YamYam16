package com.example.yamyam16.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuStatus {

	AVAILABLE("판매중"),
	SOLDOUT("품절"),
	DELETED("삭제");

	private final String description;
}
