package com.example.yamyam16.store.repository;

import com.example.yamyam16.auth.common.exception.UserErrorCode;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.exception.UserException;
import com.example.yamyam16.store.dto.response.SearchStoreResponseDto;
import com.example.yamyam16.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long> {
    //n+1 문제 해결 방법 2가지
    @EntityGraph(attributePaths = {"user_id"})
    Page<Store> findAllByIsDeleteFalseOrderByCreateAtDesc(Pageable pageable);

    @Query("SELECT new com.example.yamyam16.store.dto.response.SearchStoreResponseDto(s.id, s.name, u.name) FROM Store s JOIN s.owner u WHERE s.isDelete = false")
    Page<SearchStoreResponseDto> findStoreDtos(Pageable pageable);


    default Store findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    Long countByUserAndisDeleteFalse(User user);
//
//    @Query("SELECT AVG(r.grade) FROM Review r WHERE r.store.id = :store_id")
//    Double findAverageGradeByStoreId(@Param("store_id") Long storeId);

}



