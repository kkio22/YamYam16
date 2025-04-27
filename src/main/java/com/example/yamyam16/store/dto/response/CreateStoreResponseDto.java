package com.example.yamyam16.store.dto.response;

import org.springframework.stereotype.Service;

import com.example.yamyam16.store.entity.Store;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateStoreResponseDto {

	private Long id;
	@NotBlank(message = "상호명을 입력해주세요")
	private String name;
	@NotBlank(message = "개장시간을 입력해주세요")
	private Long open_time;
	@NotBlank(message = "마감시간을 입력해주세요")
	private Long close_time;
	@NotBlank(message = "최소주문금액을 입력해주세요")
	private Long minOrderPrice;
	@NotBlank(message = "카테고리를 입력해주세요")
	private String category;
	private String notice;

	private CreateStoreResponseDto(Store store) {
		this.id = store.getId();
		this.name = store.getName();
		this.open_time = store.getOpenTime();
		this.close_time = store.getCloseTime();
		this.minOrderPrice = store.getMinOrderPrice();
		this.category = store.getCategory().name();
		this.notice = store.getNotice();
	}

	public static CreateStoreResponseDto fromStoreToDto(Store store) {
		return new CreateStoreResponseDto(store);
	}
}
