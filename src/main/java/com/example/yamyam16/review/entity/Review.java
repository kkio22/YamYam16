package com.example.yamyam16.review.entity;

import com.example.yamyam16.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
    private int grade;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)//이게 디폴트값 -> 유저라는 완성된 객체를 나중에 불러오도록 -> 주로 store만조회하고 싶을때 씀 -> 필요할때만 불러오는
    @JoinColumn(name = "store_id") // userid로테이블에 저장이 되고 아래 객체를 테이블의 해당 타입으로 변하게 해주는게 이 두개
    private Store store;
}
