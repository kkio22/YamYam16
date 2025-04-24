package com.example.yamyam16.auth.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.example.yamyam16.auth.common.exception.UserErrorCode;
import com.example.yamyam16.auth.dto.request.LoginRequestDto;
import com.example.yamyam16.auth.dto.request.SignUpRequestDto;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.exception.UserException;
import com.example.yamyam16.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class UserCheckAspect {
	private final UserRepository userRepository;

	//pointcut Expression : AOP가 어떤 메서드 앞에서 동작할지 지정하는 문장
	//@Before("@annotation(com.example.CheckUserByEmail)") : 어노테이션이 붙은 메서드 대상으로 한다는 뜻
	// -> @CheckUserByEmail 붙은 메서드에서 실행
	// && args(email,..)
	// 메서드의 파라미터 중 첫 번째 email 지정
	// args(...) 메서드 인자를 지정하는 표현식으로
	// @CheckUserById
	// public void updateNickname(Long userId, String nickname) { ... }
	// -> 	•	첫 번째 인자 userId를 AOP가 받아서 사용할 수 있게 됨.
	// 정리 : “CheckUserByEmail 어노테이션이 붙어 있고, 첫 번째 인자가 email인 메서드가 실행되기 전에 이 로직을 실행해줘!”
	/*@Before("@annotation(com.example.yamyam16.auth.common.annotation.CheckUserDeleted) && args(email)")
	public void checkUserDeletedByEmail(String email) {
		userRepository.findByEmail(email).filter(User::isDeleted)
			.ifPresent(user -> {
				throw new UserException(UserErrorCode.USER_ALREADY_DELETED);
			});
	}

	@Before("@annotation(com.example.yamyam16.auth.common.annotation.CheckUserDeleted) && args(userId)")
	public void checkUserDeletedById(Long userId) {
		userRepository.findById(userId).filter(User::isDeleted)
			.ifPresent(user -> {
				throw new UserException(UserErrorCode.USER_ALREADY_DELETED);
			});
	}

	@Before("@annotation(com.example.yamyam16.auth.common.annotation.CheckUserDeleted) && args(requestDto)")
	public void checkUserDeletedBySignUpRequestDto(SignUpRequestDto requestDto) {
		userRepository.findByEmail(requestDto.getEmail()).filter(User::isDeleted)
			.ifPresent(user -> {
				throw new UserException(UserErrorCode.USER_ALREADY_DELETED);
			});
	}

	@Before("@annotation(com.example.yamyam16.auth.common.annotation.CheckUserDeleted) && args(requestDto)")
	public void checkUserDeletedByLoginRequestDto(LoginRequestDto requestDto) {
		userRepository.findByEmail(requestDto.getEmail()).filter(User::isDeleted)
			.ifPresent(user -> {
				throw new UserException(UserErrorCode.USER_ALREADY_DELETED);
			});
	}*/
	@Before("@annotation(com.example.yamyam16.auth.common.annotation.CheckUserDeleted) && args(arg,..)")
	public void checkUserDeleted(Object arg) {
		if (arg instanceof String email) {
			checkDeletedByEmail(email);
		} else if (arg instanceof Long userId) {
			checkDeletedByUserId(userId);
		} else if (arg instanceof SignUpRequestDto dto) {
			checkDeletedByEmail(dto.getEmail());
		} else if (arg instanceof LoginRequestDto dto) {
			checkDeletedByEmail(dto.getEmail());
		}
	}

	private void checkDeletedByEmail(String email) {
		userRepository.findByEmail(email)
			.filter(User::isDeleted)
			.ifPresent(user -> {
				throw new UserException(UserErrorCode.USER_ALREADY_DELETED);
			});
	}

	private void checkDeletedByUserId(Long userId) {
		userRepository.findById(userId)
			.filter(User::isDeleted)
			.ifPresent(user -> {
				throw new UserException(UserErrorCode.USER_ALREADY_DELETED);
			});
	}
}
