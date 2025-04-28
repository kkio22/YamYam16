package com.example.yamyam16.review.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewResponseDto {

	private Long id;
	private String content;
	private int grade;
	private LocalDateTime createdAt;

	public ReviewResponseDto(Long id, String content, int grade, LocalDateTime createdAt) {
		this.id = id;
		this.content = content;
		this.grade = grade;
		this.createdAt = createdAt;
	}

}
