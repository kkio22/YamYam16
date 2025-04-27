package com.example.yamyam16.store.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.review.entity.Review;
import com.example.yamyam16.store.dto.request.CreateStoreRequestDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
	private Long openTime;

	@Column(nullable = false, length = 30)
	private Long closeTime;

	@Column(nullable = false, length = 30)
	private Long minOrderPrice;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private CategoryType category;

	@Column(length = 30)
	private String notice;

	private boolean isDelete;

	//양방향매핑관계
	//유저
	@ManyToOne(fetch = FetchType.LAZY)//이게 디폴트값 -> 유저라는 완성된 객체를 나중에 불러오도록 -> 주로 store만조회하고 싶을때 씀 -> 필요할때만 불러오는
	@JoinColumn(name = "user_id") // userid로테이블에 저장이 되고 아래 객체를 테이블의 해당 타입으로 변하게 해주는게 이 두개
	private User user;

	//리뷰
	@OneToMany(mappedBy = "store",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<Review> reviews = new ArrayList<>();

	// 메뉴
	@OneToMany(mappedBy = "store",
		cascade = CascadeType.ALL,   // 부모 → 자식으로 영속성 전이 객체
		orphanRemoval = true)        // 컬렉션에서 빠지면 자식 레코드 삭제 컬렉션
	private List<Menu> menus = new ArrayList<>();

	// 생성자
	public Store(CreateStoreRequestDto dto, User user) {
		this.name = dto.getName();
		this.category = dto.getCategory();
		this.notice = dto.getNotice();
		this.minOrderPrice = dto.getMinOrderPrice();
		this.openTime = dto.getOpen_time();
		this.closeTime = dto.getClose_time();
		this.user = user;
		this.isDelete = false;
	}

	//crud관련
	public void update(UpdateStoreRequestDto dto) {
		this.name = dto.getStorename();
		this.openTime = dto.getOpen_time();
		this.closeTime = dto.getClose_time();
		this.minOrderPrice = dto.getMinOrderPrice();
		this.category = dto.getCategory();
		this.notice = dto.getNotice();
	}

	public void deactivate() {
		this.isDelete = true;
	}

}


