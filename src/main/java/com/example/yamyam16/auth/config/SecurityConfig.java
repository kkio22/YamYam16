package com.example.yamyam16.auth.config;

import static org.springframework.security.config.Customizer.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable()) // 세션 쓸거면 CSRF 켜는게 좋음 (보안)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/login", "/auth/signup").permitAll()
				.requestMatchers("/auth/user").hasAnyRole("USER", "OWNER")
				.anyRequest().authenticated()
			)
			.formLogin(withDefaults()); // 세션 기반 로그인 쓰는 경우
		return http.build();
	}

}
