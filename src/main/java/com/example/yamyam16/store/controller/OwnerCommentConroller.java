package com.example.yamyam16.store.controller;

import com.example.yamyam16.auth.common.consts.Const;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.store.dto.request.CreateOwnerCommmentRequstDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;
import com.example.yamyam16.store.dto.response.CreateOwnerCommentResponseDto;
import com.example.yamyam16.store.dto.response.DeactivateStoreResponseDto;
import com.example.yamyam16.store.dto.response.UpdateStoreResponseDto;
import com.example.yamyam16.store.service.OwnerCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store/ownercomment")
@RequiredArgsConstructor
public class OwnerCommentConroller {

    private final OwnerCommentService ownercommentService;

    @PostMapping
    public ResponseEntity<CreateOwnerCommentResponseDto> createOwnerComment(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @RequestBody CreateOwnerCommmentRequstDto requestDto) {

        CreateOwnerCommentResponseDto responseDto = ownercommentService.createComment(user,requestDto);

        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    }

    // 댓글삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<DeactivateStoreResponseDto> deactivateStore(
            @SessionAttribute(name = Const.LOGIN_USER) Long storeId
    ) {

        DeactivateStoreResponseDto deactivateUserResponseDto = OwnerCommentService.deleteComment(storeId);

        return new ResponseEntity<>(deactivateUserResponseDto, HttpStatus.OK);

    }

}
}
