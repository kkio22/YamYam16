package com.example.yamyam16.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

// 필터 등록 및 권한 기반 URL 보호
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	// SecurityFilterChain : Spring Security가 HTTP 요청을 처리할 때 적용할 보안 필터 목록(체인) 을 정의한 객체
	// Spring 내부에서는 요청마다 이 필터 체인을 따라 순서대로 보안 처리를 진행
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/login", "/auth/signup").permitAll() // 누구나 접근 가능
				.requestMatchers("/auth/user").hasRole("USER") // 유저만 접근 가능
				.anyRequest().authenticated() // 나머지는 모두 로그인 필요
			).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		// addFilterBefore : 스프링 필터 체인에 내가 만든 필터를 직접 끼어넣는 메서드
		//  “UsernamePasswordAuthenticationFilter 실행 직전에 내 필터를 먼저 실행시켜줘!” 라는 뜻
		// jwtAuthenticationFilter 내가 만든 JWT 인증 필터
		// UsernamePasswordAuthenticationFilter 스프링 로그인 처리 필터
		return http.build();
		// http.build()는 우리가 설정한 보안 규칙들을 적용한 “완성된 필터 체인(SecurityFilterChain)” 객체를 반환하는 것
		// 람다 DSL로 http에 설정을 덕지덕지 붙이고 나면 마지막에 .build()를 호출해서 설정을 “마감/완성” 시키는 것
		/* PizzaBuilder builder = new PizzaBuilder();
		builder.addCheese().addPepperoni().addBasil();
		Pizza pizza = builder.build(); // 🍕 최종 완성 */
	}

	// DSL : Domain-Specific Language 도메인 특화 언어
	// 특정 목적을 위해 만든 작은 전용 언어, 예시 : 보안 설정만을 위한 작은 전용 언어
	/* http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
		.requestMatchers("/login").permitAll()
      .anyRequest().authenticated()); */

	// CSRF (Cross-Site Request Forgery) : 사이트 간 요청 위조
	/* 🧠 간단 예시
	1.	철수가 로그인 중인 은행 사이트 bank.com이 있고
	2.	누가 악성 사이트 evil.com에 철수를 유도함
	3.	그 사이트가 철수 몰래 → POST /bank/transfer 요청을 날림
	4.	쿠키에는 철수의 인증정보가 남아 있으니까 요청이 먹힘 😱*/
	// 그래서 해결책: → 브라우저에서 폼 전송할 땐 CSRF 토큰을 꼭 같이 보내야 함
	// 그런데 지금은 JWT → 세션 없음, 그래서 csrf.disable() 사용
}
