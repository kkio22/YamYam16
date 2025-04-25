package com.example.yamyam16.auth.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {
	USER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
	USER_NOT_ALLOW(401, "접근 권한이 없습니다."),
	USER_DUPLICATION_EMAIL(400, "이미 가입된 이메일 정보입니다."),
	USER_WRONG_PW(400, "비밀번호를 재확인해주세요."),
	USER_SAME_PW(400, "기존 비밀번호로 변경은 불가합니다."),
	USER_ALREADY_DELETED(400, "탈퇴한 유저입니다."),
	INVALID_TOKEN(403, "유효하지 않은 토큰입니다."),
	TOKEN_NOT_FOUND(400, "토큰이 존재하지 않습니다.");;

	private final int code;
	private final String message;
}
