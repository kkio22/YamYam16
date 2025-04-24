package com.example.yamyam16.store.entity;

import com.example.yamyam16.auth.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

}
