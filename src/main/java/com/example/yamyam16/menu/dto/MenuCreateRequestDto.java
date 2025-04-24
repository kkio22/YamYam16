package com.example.yamyam16.menu.dto;

import com.example.yamyam16.MenuStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuCreateRequestDto { //requestBody로 오는 내용만 적음

	@NotBlank(message = "메뉴 이름은 필수입니다.")
	private String menuName;

	@NotNull(message = "메뉴 가격은 필수입니다.")
	@Positive(message = "메뉴 가격은 0보다 커야 합니다")
	private Long menuPrice; //@notnull 검증할 때는 integer을 사용, dto에서 자주 사용한다. int는 단순 내부 계산할 때만 사용

	@NotBlank(message = "메뉴 상태는 필수입니다.")
	private MenuStatus menuStatus; //enum 값으로 받아야 함 왜냐면 백은 프론트에서 주는 값을 어떻게 처리할지만 생각하면 됨
}
