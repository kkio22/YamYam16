package com.example.yamyam16.store.service;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.menu.repository.MenuRepository;
import com.example.yamyam16.store.common.exception.StoreCustomErrorCode;
import com.example.yamyam16.store.common.exception.StoreCustomException;
import com.example.yamyam16.store.dto.request.CreateStoreRequestDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;
import com.example.yamyam16.store.dto.response.CreateStoreResponseDto;
import com.example.yamyam16.store.dto.response.DeactivateStoreResponseDto;
import com.example.yamyam16.store.dto.response.SearchStoreResponseDto;
import com.example.yamyam16.store.dto.response.UpdateStoreResponseDto;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreService extends CommonAuthforOwner {

    private final StoreRepository storeRepository;
    public final MenuRepository menuRepository;

    //가게생성
    @Transactional
    public CreateStoreResponseDto createStore(CreateStoreRequestDto dto, User user) {

        //권환확인
        validateOwnerRole(user);
        //가게 갯수 확인
        if (storeRepository.countByUserAndIsDeleteFalse(user) >= 3) {
            throw new StoreCustomException(StoreCustomErrorCode.OVER_CREATE);
        }
        //가게생성
        Store store = new Store(dto, user);
        //저장
        Store createStore = storeRepository.save(store);
        return CreateStoreResponseDto.fromStoreToDto(createStore);

    }

    //가게전체조회 : 폐업된 가게 조회 안함
    @Transactional
    public Page<SearchStoreResponseDto> getAllStores(int page, int size) {

        //pageable 객체 생성 => 페이지 번호, 페이지 갯수, 정렬 나타냄
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by("id").ascending());


        return storeRepository.findAllByIsDeleteFalseOrderByCreatedAtDesc(pageable)
                .map(SearchStoreResponseDto::fromStoreToDto); // map (반환데이터 값 :: 객체(new나 메소드))

    }

    //가게 이름으로 찾기
    @Transactional
    public Page<SearchStoreResponseDto> getAllStoresByName(String storename, int page, int size) {

        //pageable 객체 생성 => 페이지 번호, 페이지 갯수, 정렬 나타냄
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by("id").ascending());

        return storeRepository.findAllByIsDeleteFalseAndNameContainingOrderByCreatedAtDesc(storename, pageable)
                .map(SearchStoreResponseDto::fromStoreToDto); // map (반환데이터 값 :: 객체(new나 메소드))

    }

    // 오너 본인이 운영하는 가게 찾기
    public Page<SearchStoreResponseDto> getAllStoresByOwner(User user, int page, int size) {

        //권환확인
        validateOwnerRole(user);
        Long ownerId = user.getId();

        //pageable 객체 생성 => 페이지 번호, 페이지 갯수, 정렬 나타냄
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by("id").ascending());

        Page<Store> stores = storeRepository.findAllByIsDeleteFalseAndUser_IdOrderByCreatedAtDesc(ownerId, pageable);

        if (stores.getContent().isEmpty()) {
            throw new StoreCustomException(StoreCustomErrorCode.STORE_NOT_FOUND);
        }

        return storeRepository.findAllByIsDeleteFalseAndUser_IdOrderByCreatedAtDesc(ownerId, pageable)
                .map(SearchStoreResponseDto::fromStoreToDto); // map (반환데이터 값 :: 객체(new나 메소드))

    }

    //가게수정
    @Transactional
    public UpdateStoreResponseDto updateStoreById(Long id, UpdateStoreRequestDto dto, User user) {
        //권환확인
        validateOwnerRole(user);
        Long ownerId = user.getId();

        //본인 가게인지 확인, 유저의 가게 찾기
        Store store = storeRepository.findByIdAndUserId(id, ownerId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH));


        //해당 객체 가져와서 엔티티에서 수정
        store.update(dto);
        return new UpdateStoreResponseDto(store);
    }

    //가게삭제
    @Transactional
    public DeactivateStoreResponseDto deactivateStoreById(Long id, User user) {

        //권환확인
        validateOwnerRole(user);
        Long ownerId = user.getId();
        //가게 존재 여부

        //본인 가게인지 확인, 유저의 가게 찾기
        Store findStore = storeRepository.findByIdAndUserId(id, ownerId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_MATCH));

        findStore.deactivate();
        return new DeactivateStoreResponseDto(findStore);
    }

    // 오더에서 사용합니다
    public String findStoreName(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreCustomException(StoreCustomErrorCode.STORE_NOT_FOUND));
        return store.getName();
    }

}
