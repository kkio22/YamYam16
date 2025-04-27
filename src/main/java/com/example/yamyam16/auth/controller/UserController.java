package com.example.yamyam16.auth.controller;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.auth.common.exception.UserErrorCode;
import com.example.yamyam16.auth.dto.request.LoginRequestDto;
import com.example.yamyam16.auth.dto.request.SignUpRequestDto;
import com.example.yamyam16.auth.dto.request.UpdatePasswordRequestDto;
import com.example.yamyam16.auth.dto.response.SignUpResponseDto;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.exception.UserException;
import com.example.yamyam16.auth.repository.RefreshTokenRepository;
import com.example.yamyam16.auth.security.JwtTokenProvider;
import com.example.yamyam16.auth.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

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
			.sameSite("Strict") // 다른 도메인 접근 X
			.maxAge(Duration.ofDays(7)) // 7일간 쿠키 유지, 브라우저가 꺼져도 유지됨 (persistent cookie)
			.build();

		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return new ResponseEntity<>(tokens.get("accessToken"), HttpStatus.OK);
	}

	@PatchMapping("/user")
	public ResponseEntity<String> updatePw(@Valid @RequestBody UpdatePasswordRequestDto requestDto,
		@AuthenticationPrincipal User user) {
		userService.updatePw(user.getId(), requestDto);
		return new ResponseEntity<>("업데이트 완료", HttpStatus.OK);
	}

	@DeleteMapping("/user")
	public ResponseEntity<String> delete(@RequestBody String password,
		@AuthenticationPrincipal User user) {
		userService.deleteUser(user.getId(), password);
		return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
	}

	@PostMapping("/reissue")
	public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			throw new UserException(UserErrorCode.TOKEN_NOT_FOUND);
		}

		String refreshToken = Arrays.stream(cookies)// Cookie[]을 Stream 로 바꿔서 체이닝 가능하게 만든 것
			.filter(c -> c.getName()
				.equals("refreshToken"))// 로그인할 때 쿠키 집어넣었는데 : ResponseCookie.from("refreshToken", ...) 그떄 이름 지정해줌
			.findFirst()
			.map(Cookie::getValue)// cookie -> cookie.getValue(), 쿠키에서 "value"만 뽑아내는 것
			.orElseThrow(() -> new UserException(UserErrorCode.TOKEN_NOT_FOUND));
		/*왜 findFirst() 같은 걸 쓰냐?
		•	request.getCookies()는 단순히 쿠키 배열을 던져줄 뿐
		•	만에 하나라도 같은 이름이 여러 번 들어 있다면,
		→ 우리는 “가장 먼저 오는 것”만 신뢰하자는 보수적 설계 때문*/

		Map<String, String> newTokens = userService.reissue(refreshToken);

		ResponseCookie cookie = ResponseCookie.from("refreshToken", newTokens.get("refreshToken"))
			.httpOnly(true)
			.secure(false)
			.path("/")
			.sameSite("Strict")
			.maxAge(Duration.ofDays(7))
			.build();

		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return new ResponseEntity<>(newTokens.get("accessToken"), HttpStatus.OK);
	}
}
