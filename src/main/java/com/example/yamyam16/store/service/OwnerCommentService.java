package com.example.yamyam16.store.service;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.store.dto.request.CreateOwnerCommmentRequstDto;
import com.example.yamyam16.store.dto.request.DeleteOwnerCommentRequestDto;
import com.example.yamyam16.store.dto.response.CreateOwnerCommentResponseDto;
import com.example.yamyam16.store.repository.OwnerCommentRepository;
import com.example.yamyam16.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerCommentService extends CommonAuthforOwner {
    private final OwnerCommentRepository ownerCommentRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public CreateOwnerCommentResponseDto createComment(User userId, CreateOwnerCommmentRequstDto createDto) {
        //객체 생성

    }

    @Transactional
    public void deleteComment(User userid, DeleteOwnerCommentRequestDto deleteOwnerCommentRequestDto) {

    }


}
