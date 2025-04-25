package com.example.yamyam16.store.repository;

import com.example.yamyam16.auth.common.exception.UserErrorCode;
import com.example.yamyam16.auth.exception.UserException;
import com.example.yamyam16.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findAllByOrderByCreateAtDesc(Pageable pageable);

    default Store findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
    //쿼리 sum count
}