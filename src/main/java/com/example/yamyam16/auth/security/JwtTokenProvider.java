package com.example.yamyam16.auth.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.yamyam16.auth.dto.request.LoginRequestDto;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final UserRepository userRepository;
	@Value("${jwt.secret}")
	private String secretKey;

	private final Long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 1; // 1분 : 1초 = 1000밀리초, 60초
	private final Long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 10; // 10분

	public JwtTokenProvider(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public String createAccessToken(LoginRequestDto requestDto) {
		return createToken(requestDto, ACCESS_TOKEN_VALIDITY);
	}

	public String createAccessToken(String email) {
		return createToken(email, ACCESS_TOKEN_VALIDITY);
	}

	public String createRefreshToken(LoginRequestDto requestDto) {
		return createToken(requestDto, REFRESH_TOKEN_VALIDITY);
	}

	public String createRefreshToken(String email) {
		return createToken(email, REFRESH_TOKEN_VALIDITY);
	}

	private String createToken(String email, Long validity) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + validity);
		User findUser = userRepository.findByEmailOrElseThrow(email);

		return Jwts.builder()
			.setSubject(findUser.getEmail()) // 토큰 사용자 정보
			.claim("role", findUser.getUserType()) // claim : 토큰안에 내가 원하는 정보를 심는 기능 ("key", value) 형태
			.setIssuedAt(now) // 발급 시각
			.setExpiration(expiry) // 만료 시각
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256) // 서명(보안)
			.compact(); // 최종 JWT 문자열 생성
	}

	private String createToken(LoginRequestDto requestDto, Long validity) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + validity);
		User findUser = userRepository.findByEmailOrElseThrow(requestDto.getEmail());

		return Jwts.builder()
			.setSubject(findUser.getEmail()) // 토큰 사용자 정보
			.claim("role", findUser.getUserType()) // claim : 토큰안에 내가 원하는 정보를 심는 기능 ("key", value) 형태
			.setIssuedAt(now) // 발급 시각
			.setExpiration(expiry) // 만료 시각
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256) // 서명(보안)
			.compact(); // 최종 JWT 문자열 생성
	}
	// JWT는 header(사용 알고리즘 정보), payload(유저 정보, 발급&만료시간), signature(비밀키 서명 값) 세 부분으로 구성 됨
	// compact는 이 세 부분을 base64인코딩하고 연결한 최종 JWT문자열을 만들어 주는 메서드

	public String extractEmail(String token) {
		return Jwts.parserBuilder() // 문자열을 해석하고 검증하기 위한 빌더 객체 생성
			.setSigningKey(secretKey.getBytes())// 검증을 위한 토큰을 서명한 secretkey 제공
			.build()
			.parseClaimsJws(token) // 서명 검증, 디코딩, JSON 파싱
			.getBody() // 유저 정보, role, 만료시간 등 추출
			.getSubject();
	}

	public String extractRole(String token) {
		return (String)Jwts.parserBuilder()
			.setSigningKey(secretKey.getBytes())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("role");
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey.getBytes())
				.build()
				.parseClaimsJws(token); // 만료시간 포함 검증
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
