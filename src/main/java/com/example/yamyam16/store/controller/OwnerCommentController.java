package com.example.yamyam16.store.controller;

import com.example.yamyam16.auth.common.consts.Const;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.store.dto.request.OwnerCommmentRequestDto;
import com.example.yamyam16.store.dto.response.OwnerCommentResponseDto;
import com.example.yamyam16.store.dto.response.ReviewWithOwnerCommentResponseDto;
import com.example.yamyam16.store.service.OwnerCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ownercomment")
@RequiredArgsConstructor
public class OwnerCommentController {

    private final OwnerCommentService ownercommentService;

    // 댓글 생성
    @PostMapping("/{storeId}/{reviewId}")
    public ResponseEntity<ReviewWithOwnerCommentResponseDto> createOwnerComment(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @RequestBody OwnerCommmentRequestDto requestDto) {

        // 서비스 계층을 호출하여 댓글 생성 및 DTO 변환
        ReviewWithOwnerCommentResponseDto responseDto = ownercommentService.createComment(user, storeId, reviewId, requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }


    //댓글 수정
    @PatchMapping("/{storeId}/{reviewId}")
    public ResponseEntity<OwnerCommentResponseDto> updateOwnerComment(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @RequestBody OwnerCommmentRequestDto requestDto) {

        // 댓글 수정 서비스 호출
        OwnerCommentResponseDto responseDto = ownercommentService.updateComment(user, storeId, reviewId, requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
//
//    // 댓글 조회
//    @GetMapping("/{storeId}/{reviewId}")
//    public ResponseEntity<OwnerCommentResponseDto> getOwnerComment(
//            @SessionAttribute(name = Const.LOGIN_USER) User user,
//            @PathVariable Long storeId,
//            @PathVariable Long reviewId
//
//    ) {
//
//        // 댓글 조회 서비스 호출
//        OwnerCommentResponseDto responseDto = ownercommentService.getOwnerCommentByReviewId(user, storeId, reviewId);
//
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }

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


