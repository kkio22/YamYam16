package com.example.yamyam16.review.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
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


    public Review() {
    }

}
