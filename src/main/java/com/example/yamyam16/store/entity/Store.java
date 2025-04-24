package com.example.yamyam16.store.entity;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private Long opentime;

    @Column(nullable = false, length = 30)
    private Long closetime;

    @Column(nullable = false, length = 30)
    private Long minprice;

    private boolean isDelete;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(nullable = false, length = 30)
    private String notice;

    //양방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 자식 컬렉션을 가진 부모‑측 매핑
    @OneToMany(mappedBy = "store",
            cascade = CascadeType.ALL,   // 부모 → 자식으로 영속성 전이
            orphanRemoval = true)        // 컬렉션에서 빠지면 자식 레코드 삭제
    private List<Menu> items = new ArrayList<>();

}
