package com.example.yamyam16.store.entity;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.review.entity.Review;
import com.example.yamyam16.store.dto.request.CreateStoreRequestDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;
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

    //양방향매핑관계
    //유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //리뷰
    @OneToMany(mappedBy = "store",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // 메뉴
    @OneToMany(mappedBy = "store",
            cascade = CascadeType.ALL,   // 부모 → 자식으로 영속성 전이
            orphanRemoval = true)        // 컬렉션에서 빠지면 자식 레코드 삭제
    private List<Menu> items = new ArrayList<>();


    // 생성자
    public Store(CreateStoreRequestDto dto, User user) {
        this.name = dto.getName();
        this.category = dto.getCategory();
        this.notice = dto.getNotice();
        this.minprice = dto.getMinOrderPrice();
        this.opentime = dto.getOpen_time();
        this.closetime = dto.getClose_time();
        this.user = user;
        this.isDelete = false;
    }

    //crud관련
    public void update(UpdateStoreRequestDto updatePostsRequestDto) {
        //todo 이거 고칠것
    }

    public void deactivate() {
        this.isDelete = true;
    }

}


