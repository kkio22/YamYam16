package com.example.yamyam16.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.yamyam16.auth.common.annotation.CheckUserDeleted;
import com.example.yamyam16.auth.common.exception.UserErrorCode;
import com.example.yamyam16.auth.config.PasswordEncoder;
import com.example.yamyam16.auth.dto.request.LoginRequestDto;
import com.example.yamyam16.auth.dto.request.SignUpRequestDto;
import com.example.yamyam16.auth.dto.request.UpdatePasswordRequestDto;
import com.example.yamyam16.auth.dto.response.LoginResponseDto;
import com.example.yamyam16.auth.dto.response.SignUpResponseDto;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.entity.UserType;
import com.example.yamyam16.auth.exception.UserException;
import com.example.yamyam16.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@CheckUserDeleted
	public User findById(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {
			throw new UserException(UserErrorCode.USER_NOT_FOUND);
		}

		User findUser = optionalUser.get();
		return findUser;
	}

	@Test
	@DisplayName("회원가입 성공")
	void signUp() {
		// given
		SignUpRequestDto requestDto = new SignUpRequestDto(
			UserType.USER, "test@example.com", "Password1!", "Test");

		when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class)))
			.thenReturn(new User(requestDto.getUserType(), requestDto.getEmail(), "encodedPassword", requestDto.getNickname()));

		// when
		SignUpResponseDto responseDto = userService.signUp(requestDto);

		// then
		assertThat(responseDto.getEmail()).isEqualTo("test@example.com");
		assertThat(responseDto.getNickname()).isEqualTo("Test");
	}

	@Test
	@DisplayName("로그인 성공")
	void login() {
		// given
		LoginRequestDto requestDto = new LoginRequestDto("test@example.com", "Password1!");
		User user = new User(UserType.USER, "test@example.com", "encodedPassword", "Test");

		when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);

		// when
		LoginResponseDto responseDto = userService.login(requestDto);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getId()).isEqualTo(user.getId());
	}

	@Test
	@DisplayName("아이디로 유저 찾기")
	void findById() {
		String email = "existing@example.com";
		User mockUser = new User(UserType.USER,email,"Password1!","Test");

		ReflectionTestUtils.setField(mockUser, "id", 1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

		User foundUser = userService.findById(1L);

		assertThat(foundUser).isNotNull();
		assertThat(foundUser.getEmail()).isEqualTo(email);

	}

	@Test
	@DisplayName("회원 탈퇴 (소프트 삭제) 성공")
	void deleteUser() {
		// given
		User user = new User(UserType.USER, "test@example.com", "encodedPassword", "Test");
		when(userRepository.findByIdOrElseThrow(user.getId())).thenReturn(user);
		when(passwordEncoder.matches("Password1!", user.getPassword())).thenReturn(true);

		// when
		userService.deleteUser(user.getId(), "Password1!");

		// then
		assertTrue(user.isDeleted());
		assertNotNull(user.getDeletedAt());
	}

	@Test
	@DisplayName("비밀번호 변경 성공")
	void updatePw() {
		// given
		User user = new User(UserType.USER, "test@example.com", "encodedOldPassword", "Test");
		UpdatePasswordRequestDto requestDto = new UpdatePasswordRequestDto("oldPassword1!", "newPassword1!");

		when(userRepository.findByIdOrElseThrow(user.getId())).thenReturn(user);
		when(passwordEncoder.matches("oldPassword1!", user.getPassword())).thenReturn(true);
		when(passwordEncoder.matches("newPassword1!", user.getPassword())).thenReturn(false);
		when(passwordEncoder.encode("newPassword1!")).thenReturn("encodedNewPassword");

		// when
		userService.updatePw(user.getId(), requestDto);

		// then
		assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
	}

}