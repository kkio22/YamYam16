package com.example.yamyam16.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

	@Id
	private Long userId; // user Id 기준으로 저장

	@Column(nullable = false)
	private String token;

	public RefreshToken(Long userId, String token) {
		this.userId = userId;
		this.token = token;
	}

	public void updateToken(String newToken) {
		this.token = newToken;
	}
}
