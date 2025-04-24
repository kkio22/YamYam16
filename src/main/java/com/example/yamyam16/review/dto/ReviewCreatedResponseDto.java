package com.example.yamyam16.review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewCreatedResponseDto {

	private final Long id;
	private final String content;
	private final int grade;

}
