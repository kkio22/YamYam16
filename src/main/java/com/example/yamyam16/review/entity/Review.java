package com.example.yamyam16.review.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.example.yamyam16.order.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
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

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	public Review() {
	}

	public Review(String content, int grade, Order order) {
		this.content = content;
		this.grade = grade;
		this.order = order;
	}

}
