package com.example.yamyam16.store.service;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.review.entity.Review;
import com.example.yamyam16.review.repository.ReviewRepository;
import com.example.yamyam16.store.common.exception.StoreCustomErrorCode;
import com.example.yamyam16.store.common.exception.StoreCustomException;
import com.example.yamyam16.store.dto.request.OwnerCommmentRequestDto;
import com.example.yamyam16.store.dto.response.OwnerCommentResponseDto;
import com.example.yamyam16.store.entity.OwnerComment;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.OwnerCommentRepository;
import com.example.yamyam16.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerCommentService extends CommonAuthforOwner {

    private final OwnerCommentRepository ownerCommentRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    //오너 코멘트 생성
    @Transactional
    public OwnerCommentResponseDto createComment(User user, OwnerCommmentRequestDto createDto) {
        //오너인지 확인
        validateOwnerRole(user);

        //코멘트 생성
        OwnerComment comment = new OwnerComment(createDto, user);

        //저장
        OwnerComment createStore = ownerCommentRepository.save(comment);

        return OwnerCommentResponseDto.fromCommentToDto(createStore);
    }

    //오너 코멘트 조회
    @Transactional(readOnly = true)
    public OwnerCommentResponseDto getOwnerCommentByReviewId(User user, Long storeId, Long reviewId) {

        // 오너인지 확인
        validateOwnerRole(user);

        Long ownerId = user.getId();

        Store store = storeRepository.findByIdAndUserId(storeId, ownerId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH));

        // 리뷰 가져오기
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));

        // 리뷰가 내 가게에 속한 것인지 확인
        if (!review.getStore().getId().equals(store.getId())) {
            throw new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH);
        }

        // 오너 코멘트 조회
        OwnerComment comment = ownerCommentRepository.findByReview_Id(ownerId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));

        // 조회된 OwnerComment를 ResponseDto로 변환하여 반환
        return OwnerCommentResponseDto.fromCommentToDto(comment);

    }

    //오너 코멘트 수정
    @Transactional
    public OwnerCommentResponseDto updateComment(User user, Long storeId, Long reviewId, OwnerCommmentRequestDto updateDto) {
        // 오너인지 확인
        validateOwnerRole(user);

        Long ownerId = user.getId();

        Store store = storeRepository.findByIdAndUserId(storeId, ownerId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH));

        // 리뷰 가져오기
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));

        // 리뷰가 내 가게에 속한 것인지 확인
        if (!review.getStore().getId().equals(store.getId())) {
            throw new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH);
        }

        // 오너 코멘트 가져오기
        OwnerComment comment = ownerCommentRepository.findByReview_Id(reviewId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));

        // 수정할 내용 적용
        comment.updateContent(updateDto);

        // 수정된 코멘트 저장
        ownerCommentRepository.save(comment);
        return OwnerCommentResponseDto.fromCommentToDto(comment);
    }


    //오너 코멘트 삭제
    @Transactional
    public void deleteComment(User user, Long storeId, Long reviewId) {
        // 오너인지 확인
        validateOwnerRole(user);

        Long ownerId = user.getId();

        Store store = storeRepository.findByIdAndUserId(storeId, ownerId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH));
        
        // 리뷰 가져오기
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));

        // 리뷰가 내 가게에 속한 것인지 확인
        if (!review.getStore().getId().equals(store.getId())) {
            throw new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH);
        }

        // 댓글 찾기
        OwnerComment comment = ownerCommentRepository.findByReview_Id(reviewId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));

        // 댓글 삭제
        ownerCommentRepository.delete(comment);
    }


}
