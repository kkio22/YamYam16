package com.example.yamyam16.store.repository;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.store.common.exception.StoreCustomErrorCode;
import com.example.yamyam16.store.common.exception.StoreCustomException;
import com.example.yamyam16.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * 조회 페이지 전체, 단일 조회
     */
    Page<Store> findAllByIsDeleteFalseAndNameContainingOrderByCreateAtDesc(String storename, Pageable pageable);

    @Query("SELECT DISTINCT m FROM Store s "
            + "LEFT JOIN FETCH s.menus m "
            + "WHERE s.id = :storeId")
    Page<Menu> findAllByStoreIdWithMenus(@Param("storeId") Long storeId, Pageable pageable);

    /**
     * 가게 있는지 없는지 2가지 방식 - > isDelete
     */

    default Store findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_FOUND));
    }

    /**
     * 유저의 영업하고 있는 가게갯수 확인
     */

    Long countByUserAndIsDeleteFalse(User user);

    /**
     * 가게평점 확인 // 단순 데이터 이기에 fetch 안씀
     */
    //todo: 서버에서 평점 구현 할지 여기서 구현할지 고민해 봐야함
    @Query("SELECT AVG(r.grade) FROM Store s JOIN s.reviews r WHERE s.id = :store_id")
    Double findAverageGrade(@Param("store_id") Long storeId);

    Store findByUserId(Long userId);
}


