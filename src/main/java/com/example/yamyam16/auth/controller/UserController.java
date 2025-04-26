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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yamyam16.auth.common.exception.UserErrorCode;
import com.example.yamyam16.auth.dto.request.UpdatePasswordRequestDto;
import com.example.yamyam16.auth.entity.CustomUserDetails;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.exception.UserException;
import com.example.yamyam16.auth.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
	private final UserService userService;

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
			.sameSite("None") // 다른 도메인도 OK, 소셜 로그인, 결제 진행 시 설정
			.maxAge(Duration.ofDays(7))
			.build();

		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return new ResponseEntity<>(newTokens.get("accessToken"), HttpStatus.OK);
	}

	@GetMapping("/me")
	public ResponseEntity<Map<String, Object>> getCurrentUser(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		User user = customUserDetails.getUser();
		return ResponseEntity.ok(Map.of(
			"email", user.getEmail(),
			"nickname", user.getNickname(),
			"type", user.getUserType()
		));
	}
}
