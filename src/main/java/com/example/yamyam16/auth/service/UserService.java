package com.example.yamyam16.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yamyam16.auth.common.annotation.CheckUserDeleted;
import com.example.yamyam16.auth.common.exception.UserErrorCode;
import com.example.yamyam16.auth.config.PasswordEncoder;
import com.example.yamyam16.auth.dto.request.LoginRequestDto;
import com.example.yamyam16.auth.dto.request.SignUpRequestDto;
import com.example.yamyam16.auth.dto.request.UpdatePasswordRequestDto;
import com.example.yamyam16.auth.dto.response.LoginResponseDto;
import com.example.yamyam16.auth.dto.response.SignUpResponseDto;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.exception.UserException;
import com.example.yamyam16.auth.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@CheckUserDeleted
	public SignUpResponseDto signUp(@Valid SignUpRequestDto requestDto) {
		//이메일 중복 검증
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new UserException(UserErrorCode.USER_DUPLICATION_EMAIL);
		}

		//비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

		//유저 데이터 저장
		User savedUser = userRepository.save(
			new User(requestDto.getUserType(), requestDto.getEmail(), encodedPassword, requestDto.getNickname()));

		return new SignUpResponseDto(savedUser.getEmail(), savedUser.getNickname());
	}

	@CheckUserDeleted
	public LoginResponseDto login(@Valid LoginRequestDto requestDto) {
		User user = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new UserException(UserErrorCode.USER_WRONG_PW);
		}

		return new LoginResponseDto(user.getId());
	}

	@CheckUserDeleted
	public User findById(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {
			throw new UserException(UserErrorCode.USER_NOT_FOUND);
		}

		User findUser = optionalUser.get();
		return findUser;
	}

	@CheckUserDeleted
	@Transactional
	public void deleteUser(Long userId, String password) {
		User findUser = userRepository.findByIdOrElseThrow(userId);

		if (!passwordEncoder.matches(password, findUser.getPassword())) {
			throw new UserException(UserErrorCode.USER_WRONG_PW);
		}
		// userRepository.delete(findUser); // 하드 삭제

		// 소프트 삭제
		findUser.setDeleted(true);
		findUser.setDeletedAt(LocalDateTime.now());
	}

	@CheckUserDeleted
	@Transactional
	public void updatePw(Long userId, UpdatePasswordRequestDto requestDto) {
		System.out.printf("업데이트 서비스 진입 🚀🚀🚀🚀");
		User findUser = userRepository.findByIdOrElseThrow(userId);
		if (!passwordEncoder.matches(requestDto.getCurrentPw(), findUser.getPassword())) {
			throw new UserException(UserErrorCode.USER_WRONG_PW);
		}

		if (passwordEncoder.matches(requestDto.getNewPw(), findUser.getPassword())) {
			throw new UserException(UserErrorCode.USER_SAME_PW);
		}

		String encodedPw = passwordEncoder.encode(requestDto.getNewPw());
		findUser.setPassword(encodedPw);
		System.out.printf("비번 변경 완료 🚀🚀🚀🚀");
	}
}

