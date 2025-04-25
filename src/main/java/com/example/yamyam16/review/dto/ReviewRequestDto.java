package com.example.yamyam16.review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewRequestDto {

	private final Long OrderId;
	private final String content;
	private final int grade;

}
