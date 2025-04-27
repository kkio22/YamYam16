package com.example.yamyam16.store.dto.request;

import jakarta.validation.constraints.NotBlank;

public class DeactivateStoreRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;

    public DeactivateStoreRequestDto(String password) {
        this.password = password;
    }
}
