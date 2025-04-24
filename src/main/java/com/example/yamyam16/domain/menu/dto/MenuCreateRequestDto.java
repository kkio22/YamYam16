package com.example.yamyam16.domain.menu.dto;

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
	private Integer menuPrice; //@notnull 검증할 때는 integer을 사용, dto에서 자주 사용한다. int는 단순 내부 계산할 때만 사용
}
