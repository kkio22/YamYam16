package com.example.yamyam16.store.service;


import com.example.yamyam16.auth.entity.User;
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
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreService extends CommonAuthforOwner {

    private final StoreRepository storeRepository;

    //가게생성
    @Transactional
    public CreateStoreResponseDto createStore(CreateStoreRequestDto dto, User user) {

        //권환확인
        validateOwnerRole(user);
        //가게 갯수 확인
        if (storeRepository.countByUserAndIsDeleteFalse(user) > 3) {
            throw new StoreCustomException(StoreCustomErrorCode.OVER_CREATE);
        }
        //가게생성
        Store store = new Store(dto, user);
        //저장
        Store createStore = storeRepository.save(store);
        return CreateStoreResponseDto.fromStoreToDto(createStore);

    }

    //todo : 이거 수정할 것
    //가게전체조회 : 폐업된 가게 조회 안함
    @Transactional
    public Page<SearchStoreResponseDto> getAllStores(String storeName, Pageable pageable) {
        return storeRepository.findAllByIsDeleteFalseAndNameContainingOrderByCreateAtDesc(storeName, pageable)
                .map(SearchStoreResponseDto::fromStoreToDto); // map (반환데이터 값 :: 객체(new나 메소드))

    }

    //    //가게목록조회
//    @Transactional
//    public Page<SearchStoreResponseDto> getStoresByCategory() {
//
//    }


//    // 자신이 운영하는 가게 찾기
//    public Page<SearchStoreResponseDto> getAllStoresByOwner(Long userId) {
//        Page<Store> stores = storeRepository.findAllByIsDeleteFalseAndOwnerId(userId);
//
//        if (stores.isEmpty()) {
//            throw new StoreCustomException(StoreCustomErrorCode.STORE_NOT_FOUND);
//        }
//
//        return storeRepository.findAllByIsDeleteFalseAndOwnerId(userId);
//    }

    //가게수정
    @Transactional
    public UpdateStoreResponseDto updateStoreById(Long id, UpdateStoreRequestDto dto, User user) {
        //권환확인
        validateOwnerRole(user);
        //가게 찾기
        Store store = storeRepository.findByIdOrElseThrow(id);
        //해당 객체 가져와서 엔티티에서 수정
        store.update(dto);
        return new UpdateStoreResponseDto(store);
    }

    //가게삭제
    @Transactional
    public DeactivateStoreResponseDto deactivateStoreById(Long id, User user) {

        //권환확인
        validateOwnerRole(user);
        //가게 존재 여부
        Store findStore = storeRepository.findByIdOrElseThrow(id);
        findStore.deactivate();
        return new DeactivateStoreResponseDto(findStore);
    }

    // 오더에서 사용합니다
    public String findStoreName(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));
        return store.getName();
    }

}
