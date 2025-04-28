package com.example.yamyam16.store.repository;

import com.example.yamyam16.store.entity.OwnerComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerCommentRepository extends JpaRepository<OwnerComment, Long> {
    @Override
    Optional<OwnerComment> findById(Long ownerCommentId);

    Optional<OwnerComment> findByReview_Id(Long reviewId); // review 객체의 id 필드를 사용
}
