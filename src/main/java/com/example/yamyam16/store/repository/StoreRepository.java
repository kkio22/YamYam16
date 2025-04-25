package com.example.yamyam16.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.auth.common.exception.UserErrorCode;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.exception.UserException;
import com.example.yamyam16.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
	Page<Store> findAllByOrderByCreateAtDesc(Pageable pageable);

	default Store findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}

	Long user(User user);
	//쿼리 sum count
}