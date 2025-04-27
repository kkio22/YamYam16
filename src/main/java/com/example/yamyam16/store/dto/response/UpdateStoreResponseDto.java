package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.Store;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateStoreResponseDto {
	private Long id;
	private String storename;
	private Long opentime;
	private Long closetime;
	private Long minOrderPrice;
	private String category;
	@Size(max = 100, message = "공지는 30글자 이하로 입력해주세요.")
	private String notice;

	public UpdateStoreResponseDto(Store store) {
		this.id = store.getId();
		this.storename = store.getName();
		this.opentime = store.getOpenTime();
		this.closetime = store.getCloseTime();
		this.minOrderPrice = store.getMinOrderPrice();
		this.category = store.getCategory().name();
		this.notice = store.getNotice();
	}
}
