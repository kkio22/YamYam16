package com.example.yamyam16.review.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.yamyam16.order.entity.Order;
import com.example.yamyam16.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "review")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String content;
	private int grade;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "created_at", updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)//이게 디폴트값 -> 유저라는 완성된 객체를 나중에 불러오도록 -> 주로 store만조회하고 싶을때 씀 -> 필요할때만 불러오는
	@JoinColumn(name = "store_id") // userid로테이블에 저장이 되고 아래 객체를 테이블의 해당 타입으로 변하게 해주는게 이 두개
	private Store store;

	public Review(String content, int grade) {
		this.content = content;
		this.grade = grade;
	}
}
