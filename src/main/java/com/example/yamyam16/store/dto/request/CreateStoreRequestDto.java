package com.example.yamyam16.store.dto.request;

import com.example.yamyam16.store.entity.CategoryType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreRequestDto {

	@NotBlank(message = "상호명을 입력해주세요")
	private String name;
	@NotNull(message = "개장시간을 입력해주세요")
	private Long opentime;
	@NotNull(message = "마감시간을 입력해주세요")
	private Long closetime;
	@NotNull(message = "최소주문금액을 입력해주세요")
	private Long minOrderPrice;
	@NotNull(message = "카테고리를 입력해주세요")
	private CategoryType category;
	private String notice;

}
