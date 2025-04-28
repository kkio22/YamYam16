package com.example.yamyam16.auth.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.yamyam16.auth.common.consts.Const;
import com.example.yamyam16.auth.dto.request.LoginRequestDto;
import com.example.yamyam16.auth.dto.request.SignUpRequestDto;
import com.example.yamyam16.auth.dto.request.UpdatePasswordRequestDto;
import com.example.yamyam16.auth.dto.response.LoginResponseDto;
import com.example.yamyam16.auth.dto.response.SignUpResponseDto;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
		SignUpResponseDto responseDto = userService.signUp(requestDto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(
		@Valid @RequestBody LoginRequestDto requestDto,
		HttpServletRequest request
	) {
		//로그인 유저 조회
		LoginResponseDto responseDto = userService.login(requestDto);
		Long userId = responseDto.getId();
		System.out.println("유저 조회 성공 🚀🚀🚀🚀");

		//로그인 성공
		//getSession(true) : default, 세션 없으면 생성
		HttpSession session = request.getSession();

		User loginUser = userService.findById(userId);

		// 세션에 로그인 회원 정보 저장
		session.setAttribute(Const.LOGIN_USER, loginUser);
		session.setAttribute("userId", loginUser.getId());

		// 스프링 시큐리티 인증 객체 등록
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + loginUser.getUserType().name());
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(loginUser, null, List.of(authority));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// SecurityContext를 세션에 저장 (이거 추가!)
		SecurityContext securityContext = SecurityContextHolder.getContext();
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

		System.out.println("로그인 성공 🚀🚀🚀🚀");
		return new ResponseEntity<>("로그인 성공", HttpStatus.OK);

	}

	@PatchMapping("/user")
	public ResponseEntity<String> updatePw(@Valid @RequestBody UpdatePasswordRequestDto requestDto,
		@SessionAttribute(name = "loginUser") User loginUSer) {
		System.out.println("업데이트 컨트롤러 진입 🚀🚀🚀🚀");
		Long userId = loginUSer.getId();
		userService.updatePw(userId, requestDto);
		return new ResponseEntity<>("업데이트 완료", HttpStatus.OK);
	}

	@DeleteMapping("/user")
	public ResponseEntity<String> delete(@RequestBody String password,
		@SessionAttribute(name = "loginUser") User loginUser) {
		Long userId = loginUser.getId();
		userService.deleteUser(userId, password);
		return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
	}

}
