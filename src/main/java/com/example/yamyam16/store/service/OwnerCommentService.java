package com.example.yamyam16.store.service;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.review.dto.ReviewResponseDto;
import com.example.yamyam16.review.entity.Review;
import com.example.yamyam16.review.repository.ReviewRepository;
import com.example.yamyam16.store.common.exception.StoreCustomErrorCode;
import com.example.yamyam16.store.common.exception.StoreCustomException;
import com.example.yamyam16.store.dto.request.OwnerCommmentRequestDto;
import com.example.yamyam16.store.dto.response.OwnerCommentResponseDto;
import com.example.yamyam16.store.dto.response.ReviewWithOwnerCommentResponseDto;
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

    //오너 댓글 생성
    @Transactional
    public ReviewWithOwnerCommentResponseDto createComment(User user, Long storeId, Long reviewId, OwnerCommmentRequestDto createDto) {
        // 오너인지 확인
        validateOwnerRole(user);

        Long ownerId = user.getId();

        // 가게 조회 및 소유자 확인
        Store store = storeRepository.findByIdAndUserId(storeId, ownerId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH));

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));

        // 리뷰가 해당 가게 소속인지 검증
        if (!review.getStore().getId().equals(store.getId())) {
            throw new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH);
        }

        // 사장님 댓글 생성
        OwnerComment ownerComment = new OwnerComment(createDto, user);
        OwnerComment savedComment = ownerCommentRepository.save(ownerComment);

        // DTO 변환
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
                review.getId(),
                review.getContent(),
                review.getGrade(),
                review.getCreatedAt()
        );

        OwnerCommentResponseDto ownerCommentResponseDto = OwnerCommentResponseDto.fromCommentToDto(savedComment);

        // 리뷰 + 사장님 댓글 묶어서 리턴
        return new ReviewWithOwnerCommentResponseDto(reviewResponseDto, ownerCommentResponseDto);
    }

//    //오너 코멘트 조회
//    @Transactional(readOnly = true)
//    public OwnerCommentResponseDto getOwnerCommentByReviewId(User user, Long storeId, Long reviewId) {
//
//        // 오너인지 확인
//        validateOwnerRole(user);
//
//        Long ownerId = user.getId();
//
//        Store store = storeRepository.findByIdAndUserId(storeId, ownerId)
//                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH));
//
//        // 리뷰 가져오기
//        Review review = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));
//
//        // 리뷰가 내 가게에 속한 것인지 확인
//        if (!review.getStore().getId().equals(store.getId())) {
//            throw new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH);
//        }
//
//        // 오너 코멘트 조회
//        OwnerComment comment = ownerCommentRepository.findByReview_Id(ownerId)
//                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));
//
//        // 조회된 OwnerComment를 ResponseDto로 변환하여 반환
//        return OwnerCommentResponseDto.fromCommentToDto(comment);
//
//    }

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

    @Transactional
    public void deleteComment(User user, Long storeId, Long reviewId) {
        // 오너인지 확인
        validateOwnerRole(user);

        Long ownerId = user.getId();

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdAndUserId(storeId, ownerId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH));

        // 리뷰 정보 가져오기
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));

        // 리뷰가 내 가게에 속한 것인지 확인
        if (!review.getStore().getId().equals(store.getId())) {
            throw new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH);
        }

        // 오너 코멘트 가져오기
        OwnerComment comment = ownerCommentRepository.findByReview_Id(reviewId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.COMMENT_NOT_FOUND));

        // 댓글 삭제
        ownerCommentRepository.delete(comment);

    }

}
