package com.example.yamyam16.store.repository;

import com.example.yamyam16.store.entity.OwnerComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OwnerCommentRepository extends JpaRepository<OwnerComment, Long> {

    @Query("SELECT c FROM OwnerComment c " +
            "INNER JOIN FETCH c.review r " +
            "INNER JOIN FETCH c.store s " +
            "WHERE r.id = :reviewId AND s.user. = :storeId")
    Optional<OwnerComment> findOwnerCommentByUserAndReviewId(@Param("storeId") Long storeId, @Param("reviewId") Long reviewId);


}
