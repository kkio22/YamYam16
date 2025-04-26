package com.example.yamyam16.auth.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.yamyam16.auth.entity.CustomUserDetails;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	// JWT 인증 필터는 한번의 요청에 한번만 검사해야함. 중복 실행 방지를 위해 필수라고 함.
	// OncePerRequestFilter : Spring Security에서 제공하는 추상 클래스
	// 요청당 한 번만 실행되는 필터를 만들고 싶을 때 사용
	// org.springframework.web.filter.OncePerRequestFilter

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) { // 규약에 따라 토큰은 Bearer 다음에 토큰이 오는 구조임
			String token = header.substring(7); // Bearer 문자를 자른 토큰값 가져오기
			System.out.println("✅ JwtAuthenticationFilter: Authorization header = " + header);
			System.out.println("✅ JwtAuthenticationFilter: token = " + token);
			System.out.println(
				"✅ JwtAuthenticationFilter: validateToken result = " + jwtTokenProvider.validateToken(token));
			if (jwtTokenProvider.validateToken(token)) { // 토큰 검증 후 문제 없으면
				String email = jwtTokenProvider.extractEmail(token); // 이메일
				String role = jwtTokenProvider.extractRole(token); // 유저 타입 정보

				User user = userRepository.findByEmailOrElseThrow(email);
				CustomUserDetails customUserDetails = new CustomUserDetails(user);

				// Spring Security에 로그인했다 인증상태를 등록하는 과정
				// SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
				// SimpleGrantedAuthority : 역할을 표현하는 객체, "ROLE_OWNER" 같은 문자열을 감싸서 스프링이 이해할 수 있게 만듦
				// Spring Security는 문자열(“OWNER”, “USER”)로는 권한을 직접 이해하지 못함
				// 그래서 이렇게 GrantedAuthority라는 인터페이스를 구현한 객체가 필요
				// Spring Security는 기본적으로 "ROLE_" 접두사가 붙은 권한을 → hasRole("OWNER") 와 같은 표현으로 해석할 수 있도록 만들어놨음.
				UsernamePasswordAuthenticationToken auth =
					new UsernamePasswordAuthenticationToken(
						customUserDetails, null, customUserDetails.getAuthorities()
					);

				System.out.println("✅ SecurityContext에 인증 정보 저장 완료");
				// 이 사용자는 로그인된 사용자로 알려주는 인증 토큰
				// principal : 사용자 ID, 이메일 등
				// credentials : 비밀번호, 토큰은 로그인 이후 발급되니까 필요 없는 정보
				// authorities : 유저 권한 목록(권한이 여러개 가능할 수 있으니 List.of
				System.out.println("✅ JwtAuthenticationFilter: 인증 객체 생성 - email: " + email + ", role: " + role);
				SecurityContextHolder.getContext().setAuthentication(auth);
				System.out.println("✅ JwtAuthenticationFilter: 인증 객체 SecurityContext 저장 완료");
				System.out.println("✅ JWT 인증 완료: " + email);
				// 현재 요청의 인증 정보를 보관하는 저장소
				// 컨트롤러에서 @AuthenticationPrincipal 또는 Authentication authentication으로 꺼낼 수 있음
			} else {
				System.out.println("❌ 토큰 유효성 검사 실패");
			}
		} else {
			System.out.println("❌ Authorization 헤더 없음 또는 Bearer 형식 아님");
		}
		filterChain.doFilter(request, response);
	}
}
