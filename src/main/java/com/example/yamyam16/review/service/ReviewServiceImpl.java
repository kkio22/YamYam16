package com.example.yamyam16.review.service;

import org.springframework.stereotype.Service;

import com.example.yamyam16.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;

}
