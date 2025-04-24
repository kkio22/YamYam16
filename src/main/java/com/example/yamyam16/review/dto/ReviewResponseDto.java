package com.example.yamyam16.review.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewResponseDto {

	private final String content;
	private final Integer grade;
	private final LocalDateTime createdAt;

}
