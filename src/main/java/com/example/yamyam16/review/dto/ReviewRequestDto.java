package com.example.yamyam16.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ReviewRequestDto {

	private final Long orderId;
	private final String content;
	private final int grade;

}
