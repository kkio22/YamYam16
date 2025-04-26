package com.example.yamyam16.auth.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.yamyam16.auth.entity.RefreshToken;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.repository.RefreshTokenRepository;
import com.example.yamyam16.auth.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	// AuthenticationSuccessHandler란?
	// onAuthenticationSuccess 메서드 기능은?

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		System.out.println("🎯 onAuthenticationSuccess 실행됨");

		// Authentication 객체는 무슨 역할?
		// 왜 getPrincipal하는데 강제 형변환함?
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		// 카카오 응답 구조에서 이메일, 닉네임 꺼내기
		Map<String, Object> attributes = oAuth2User.getAttributes();
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");

		String email = (String)kakaoAccount.get("email");
		User user = userRepository.findByEmailOrElseThrow(email);
		System.out.println("📩 attributes = " + attributes);
		System.out.println("📧 email = " + email);

		// JWT 발급
		String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
		String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
		System.out.println("🟢 accessToken = " + accessToken);

		// 리프레시 토큰 저장 or 갱신
		refreshTokenRepository.findByUserId(user.getId())
			.ifPresentOrElse(
				exist -> {
					exist.updateToken(refreshToken);
					refreshTokenRepository.save(exist);
				}, () -> refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken))
			);

		// 쿠키로 리프레시 토큰 내려주기
		ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.secure(false)
			.path("/")
			.sameSite("None")
			.maxAge(Duration.ofDays(7))
			.build();

		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		// JSON 응답
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		String body = String.format("{\"accessToken\": \"%s\"}", accessToken);
		response.getWriter().write(body);
		return;
	}
}
