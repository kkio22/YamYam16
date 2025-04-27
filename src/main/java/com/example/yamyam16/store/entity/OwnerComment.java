package com.example.yamyam16.store.entity;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.review.entity.Review;
import com.example.yamyam16.store.dto.request.OwnerCommmentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
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

    //유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private User user;

    // unique 설정 일대일 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", unique = true)
    private Review review;

    public OwnerComment(OwnerCommmentRequestDto createDto, User user) {
        this.content = createDto.getContent();
        this.createAt = LocalDateTime.now();
        this.user = user;
    }
}
