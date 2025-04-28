package com.example.yamyam16.store.entity;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.review.entity.Review;
import com.example.yamyam16.store.dto.request.OwnerCommmentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ownercomment")
public class OwnerComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 리뷰와 일대일 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", unique = true)
    private Review review;

    // 생성자 (댓글 내용과 유저만 받아오는 생성자)
    public OwnerComment(OwnerCommmentRequestDto createDto, User user) {
        this.content = createDto.getContent();
        this.createAt = LocalDateTime.now();
        this.user = user;
    }

    // 생성자 (댓글 내용, 유저, 리뷰를 모두 받아오는 생성자)
    public OwnerComment(String content, LocalDateTime createAt, User user, Review review) {
        this.content = content;
        this.createAt = createAt;
        this.user = user;
        this.review = review;
    }

    // 리뷰를 설정하는 메소드
    public void setReview(Review review) {
        this.review = review;
    }

    // 댓글 수정 메소드
    public void updateContent(OwnerCommmentRequestDto updateDto) {
        this.content = updateDto.getContent();
        this.createAt = LocalDateTime.now();
    }
}