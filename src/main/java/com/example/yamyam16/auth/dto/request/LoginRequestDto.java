package com.example.yamyam16.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
	@NotBlank(message = "필수 입력값을 입력해주세요.")
	private final String email;

	@NotBlank(message = "필수 입력값을 입력해주세요.")
	private final String password;
}
