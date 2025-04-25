package com.example.yamyam16.store.service;

import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;

	//가게생성
	// @Override
	// public CreateStoreRequestDto signUp(CreateStoreRequestDto dto) {

	// }

	//가게조회

	//가게수정

	//가게삭제
	// @Override
	// @Transactional
	// public DeactivateStoreResponseDto deactivateUser(Long id, DeactivateStoreRequestDto dto) {
	//

	// }

}
