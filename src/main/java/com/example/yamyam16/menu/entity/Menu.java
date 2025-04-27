package com.example.yamyam16.menu.entity;

import java.time.LocalDateTime;

import com.example.yamyam16.menu.MenuStatus;
import com.example.yamyam16.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity //store와 menu는 1:n 관계
@Table(name = "menu") //데이터 베이스 테이블 이름 설정
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 설정
	private Long id;

	@Column(nullable = false)
	private String menuName;

	@Column(nullable = false)
	private long menuPrice; //이거 integer인지 long인지 확인 한번 하기

	@Column//null은 기본적으로 true임
	private LocalDateTime deleteAt;

	@Column
	private boolean is_deleted = false;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MenuStatus menuStatus;

	@Setter
	@ManyToOne
	@JoinColumn(name = "store_id") //연관관계 매핑
	private Store store;

	public Menu() {
	}

	public Menu(String menuName, long menuPrice, MenuStatus menuStatus) {
		this.menuName = menuName;
		this.menuPrice = menuPrice;
		this.menuStatus = menuStatus;
	}

	public void updateMenu(String menuName, long menuPrice, MenuStatus menuStatus) {
		this.menuName = menuName;
		this.menuPrice = menuPrice;
		this.menuStatus = menuStatus;
	}

	public void deleteMenu() {
		this.is_deleted = true;
		this.deleteAt = LocalDateTime.now();
		this.menuStatus = MenuStatus.DELETED;
	}

}
