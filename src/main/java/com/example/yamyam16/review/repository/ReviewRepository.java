package com.example.yamyam16.review.repository;

import com.example.yamyam16.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.grade) FROM Review r WHERE r.store.id = :store_id")
    Double findAverageGradeByStoreId(@Param("store_id") Long storeId);

}
