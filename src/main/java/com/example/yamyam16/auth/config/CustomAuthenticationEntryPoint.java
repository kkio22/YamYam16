package com.example.yamyam16.auth.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		log.error("Unauthorized error: {}", authException.getMessage());

		// 직접 에러 응답 작성
		response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
		response.setContentType("application/json;charset=UTF-8");

		String body = """
            {
              "code": 401,
              "error": "UNAUTHORIZED",
              "message": "로그인이 필요합니다."
            }
            """;

		response.getWriter().write(body);

	}
}
