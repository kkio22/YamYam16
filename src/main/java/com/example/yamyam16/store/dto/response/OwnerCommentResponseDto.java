package com.example.yamyam16.store.dto.response;

import com.example.yamyam16.store.entity.OwnerComment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class OwnerCommentResponseDto {
    @JsonProperty("commentId")
    private Long id;
    private String content;
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private LocalDateTime createAt;

    private OwnerCommentResponseDto(OwnerComment ownerComment) {
        this.id = ownerComment.getId();
        this.content = ownerComment.getContent();
        this.createAt = ownerComment.getCreateAt();
    }

    public static OwnerCommentResponseDto fromCommentToDto(OwnerComment ownerComment) {

        return new OwnerCommentResponseDto(ownerComment);

    }
}
