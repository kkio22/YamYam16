package com.example.yamyam16.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.yamyam16.auth.common.UserErrorCode;
import com.example.yamyam16.auth.config.PasswordEncoder;
import com.example.yamyam16.auth.dto.request.LoginRequestDto;
import com.example.yamyam16.auth.dto.request.SignUpRequestDto;
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

	public SignUpResponseDto signUp(@Valid SignUpRequestDto dto) {
		//이메일 중복 검증
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new UserException(UserErrorCode.USER_DUPLICATION_EMAIL);
		}

		//비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(dto.getPassword());

		//유저 데이터 저장
		User savedUser = userRepository.save(
			new User(dto.getUserType(), dto.getEmail(), encodedPassword, dto.getNickname()));

		return new SignUpResponseDto(savedUser.getEmail(), savedUser.getNickname());
	}

	public LoginResponseDto login(@Valid LoginRequestDto requestDto) {
		User user = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new UserException(UserErrorCode.USER_WRONG_PW);
		}

		return new LoginResponseDto(user.getId());
	}

	public User findById(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {
			throw new UserException(UserErrorCode.USER_NOT_FOUND);
		}

		User findUser = optionalUser.get();
		return findUser;
	}

	public void deleteUser(Long userId, String password) {
		User findUser = userRepository.findByIdOrElseThrow(userId);
		if (!passwordEncoder.matches(password, findUser.getPassword())) {
			throw new UserException(UserErrorCode.USER_WRONG_PW);
		}
		userRepository.delete(findUser);
	}
}

