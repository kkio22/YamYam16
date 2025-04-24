package com.example.yamyam16.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.auth.common.exception.UserErrorCode;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.exception.UserException;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	// Optional<User> 로 리턴하는 이유는:
	// 이메일이 존재하지 않을 수도 있으니까 NullPointerException 방지용

	default User findByEmailOrElseThrow(String email) {
		return findByEmail(email).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}

	default User findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}

	boolean existsByEmail(String email);
}
