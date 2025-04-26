package com.example.yamyam16.auth.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.auth.dto.request.LoginRequestDto;
import com.example.yamyam16.auth.dto.request.SignUpRequestDto;
import com.example.yamyam16.auth.dto.response.SignUpResponseDto;
import com.example.yamyam16.auth.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
		SignUpResponseDto responseDto = userService.signUp(requestDto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(
		@Valid @RequestBody LoginRequestDto requestDto,
		HttpServletResponse response
	) {
		Map<String, String> tokens = userService.login(requestDto);
		ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
			.httpOnly(true) // 쿠키를 JavaScript에서 접근 불가하게 만듦, XSS(스크립트 공격)로부터 토큰을 보호하는 기본 보안 설정
			.secure(false) //  HTTPS에서만 전송될지 여부
			.path("/") // "/"는 모든 요청 경로에 대해 자동 전송
			.sameSite("None") // 다른 도메인도 OK, 소셜 로그인, 결제 진행 시 설정
			.maxAge(Duration.ofDays(7)) // 7일간 쿠키 유지, 브라우저가 꺼져도 유지됨 (persistent cookie)
			.build();

		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return new ResponseEntity<>(tokens.get("accessToken"), HttpStatus.OK);
	}
}
