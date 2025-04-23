package com.example.yamyam16.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdatePasswordRequestDto {
	private String currentPw;

	@NotBlank(message = "필수 입력값을 입력해주세요.")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}:\";'<>?,./]).{8,}$",
		message = "비밀번호는 대소문자, 숫자, 특수문자를 최소 1자 이상 포함하고 8자 이상이어야 합니다."
	)
	private String newPw;
}
