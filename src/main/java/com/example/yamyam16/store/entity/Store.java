package com.example.yamyam16.store.entity;

import com.example.yamyam16.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 30)
    private Long open_time;

    @Column(nullable = false, length = 30)
    private Long close_time;

    @Column(nullable = false, length = 30)
    private String min_price;

    private boolean isDelete;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(nullable = false, length = 30)
    private String notice;

    //양방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
