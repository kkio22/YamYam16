package com.example.yamyam16.store.dto.response;

import lombok.Getter;
import org.example.expert.domain.user.dto.response.UserResponse;

@Getter
public class CreateStoreResponse {

    private final Long id;
    private final String contents;
    private final UserResponse user;

    public CreateStoreResponse(Long id, String contents, UserResponse user) {
        this.id = id;
        this.contents = contents;
        this.user = user;
    }
}
