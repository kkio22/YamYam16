package com.example.yamyam16.store.controller;

import com.example.yamyam16.auth.common.consts.Const;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.store.dto.request.CreateOwnerCommmentRequstDto;
import com.example.yamyam16.store.dto.response.CreateOwnerCommentResponseDto;
import com.example.yamyam16.store.service.OwnerCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ownercomment")
@RequiredArgsConstructor
public class OwnerCommentConroller {

    private final OwnerCommentService ownercommentService;

    @PostMapping
    public ResponseEntity<CreateOwnerCommentResponseDto> createOwnerComment(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @RequestBody CreateOwnerCommmentRequstDto requestDto) {

        CreateOwnerCommentResponseDto responseDto = ownercommentService.createComment(user, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

    }

    // 댓글삭제
    @DeleteMapping("/{storeId}/{reviewId}")
    public ResponseEntity<String> deactivateStore(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @PathVariable Long storeId,
            @PathVariable Long reviewId
    ) {

        ownercommentService.deleteComment(user, storeId, reviewId);
        return ResponseEntity.ok("댓글이 삭제되었습니다");

    }

}

