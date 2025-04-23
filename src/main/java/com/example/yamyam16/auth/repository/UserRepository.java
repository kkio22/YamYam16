package com.example.yamyam16.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.yamyam16.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	// Optional<User> 로 리턴하는 이유는:
	// 이메일이 존재하지 않을 수도 있으니까 NullPointerException 방지용

	default User findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."));
	}
}
