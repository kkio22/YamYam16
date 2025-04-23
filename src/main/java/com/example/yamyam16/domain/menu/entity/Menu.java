package com.example.yamyam16.domain.menu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

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
	private int menuPrice; //이거 integer인지 long인지 확인 한번 하기

	@ManyToOne
	@JoinColumn(name = "store_id") //연관관계 매핑
	private Store store;

	public Menu() {
	}

	public Menu(String menuName, int menuPrice) {
		this.menuName = menuName;
		this.menuPrice = menuPrice;
	}

}
