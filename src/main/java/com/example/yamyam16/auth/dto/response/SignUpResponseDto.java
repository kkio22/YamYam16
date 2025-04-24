package com.example.yamyam16.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponseDto {
	private final String email;
	private final String nickname;
}
