package com.example.yamyam16.review.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

}
