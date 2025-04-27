package com.example.yamyam16.auth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserType userType;

	@Column(nullable = false, unique = true)
	@Email
	private String email;

	@Setter
	@Column(nullable = false)
	private String password;

	private String nickname;

	@Setter
	private boolean deleted;

	@Setter
	private LocalDateTime deletedAt;

	// 필드 초기화용 생성자
	public User(UserType userType, String email, String password, String nickname) {
		this.userType = userType;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.deleted = false;
	}
}
