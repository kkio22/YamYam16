package com.example.yamyam16.auth.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.yamyam16.auth.config.PasswordEncoder;
import com.example.yamyam16.auth.dto.request.LoginRequestDto;
import com.example.yamyam16.auth.dto.request.SignUpRequestDto;
import com.example.yamyam16.auth.dto.response.LoginResponseDto;
import com.example.yamyam16.auth.dto.response.SignUpResponseDto;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SignUpResponseDto signUp(@Valid SignUpRequestDto dto) {
		//비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(dto.getPassword());

		//유저 데이터 저장
		User savedUser = userRepository.save(
			new User(dto.getUserType(), dto.getEmail(), encodedPassword, dto.getNickname()));

		return new SignUpResponseDto(savedUser.getEmail(), savedUser.getNickname());
	}

	public LoginResponseDto login(@Valid LoginRequestDto requestDto) {
		User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."));

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
		}

		return new LoginResponseDto(user.getId());
	}

	public User findById(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.");
		}

		User findUser = optionalUser.get();
		return findUser;
	}

	public void deleteUser(Long userId, String password) {
		User findUser = userRepository.findByIdOrElseThrow(userId);
		System.out.println("입력된 비밀번호: [" + password + "]");
		System.out.println("DB의 비밀번호: [" + findUser.getPassword() + "]");
		System.out.println("일치 여부: " + passwordEncoder.matches(password, findUser.getPassword()));
		if (!passwordEncoder.matches(password, findUser.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
		}
		userRepository.delete(findUser);
	}
}

