package com.example.yamyam16.store.service;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.store.common.exception.StoreCustomErrorCode;
import com.example.yamyam16.store.common.exception.StoreCustomException;
import com.example.yamyam16.store.dto.request.OwnerCommmentRequstDto;
import com.example.yamyam16.store.dto.response.OwnerCommentResponseDto;
import com.example.yamyam16.store.entity.OwnerComment;
import com.example.yamyam16.store.repository.OwnerCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerCommentService extends CommonAuthforOwner {

    private final OwnerCommentRepository ownerCommentRepository;

    @Transactional
    public OwnerCommentResponseDto createComment(User user, OwnerCommmentRequstDto createDto) {
        //오너인지 확인
        validateOwnerRole(user);

        //코멘트 생성
        OwnerComment comment = new OwnerComment(createDto, user);

        //저장
        OwnerComment createStore = ownerCommentRepository.save(comment);

        return OwnerCommentResponseDto.fromCommentToDto(createStore);
    }

    @Transactional
    public void deleteComment(User user, Long commentId, Long reviewId) {
        //오너인지 확인
        validateOwnerRole(user);
        //오너 가게중
        Long ownerId = user.getId();

        OwnerComment comment = ownerCommentRepository.findById(commentId)
                .orElseThrow(() -> new StoreCustomException((StoreCustomErrorCode.COMMENT_NOT_FOUND)));
        if (!comment.getUser().getId().equals(ownerId)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        ownerCommentRepository.delete(comment);
    }


}
