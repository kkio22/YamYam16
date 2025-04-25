package com.example.yamyam16.store.service;


import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.repository.UserRepository;
import com.example.yamyam16.store.dto.request.CreateStoreRequestDto;
import com.example.yamyam16.store.dto.request.DeactivateStoreRequestDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;
import com.example.yamyam16.store.dto.response.CreateStoreResponseDto;
import com.example.yamyam16.store.dto.response.DeactivateStoreResponseDto;
import com.example.yamyam16.store.dto.response.SearchStoreResponseDto;
import com.example.yamyam16.store.dto.response.UpdateStoreResponseDto;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    //가게생성
    @Transactional
    public CreateStoreResponseDto createStore(CreateStoreRequestDto dto, User user) {

        //생성
        Long userId = user.getId();
        Store store = new Store(dto, user);

        //저장
        Store createStore = storeRepository.save(store);
        return new CreateStoreResponseDto(createStore);

    }

    //가게전체조회
    @Transactional
    public Page<SearchStoreResponseDto> getAllStores(Pageable pageable) {
        return storeRepository.findAllByOrderByCreateAtDesc(pageable)
                .map(SearchStoreResponseDto::new);
    }

    //가게목록조회
    @Transactional
    public Page<SearchStoreResponseDto> getStoresByCategory() {

    }

    //가게수정
    @Transactional
    public UpdateStoreResponseDto updateStoreById(Long id, UpdateStoreRequestDto dto) {
        Store store = storeRepository.findByIdOrElseThrow(id);
        store.update(dto);
        Store updateStore = storeRepository.save(store);
        return new UpdateStoreResponseDto(updateStore);
    }

    //가게삭제 -> 소프트 딜리트임
    @Transactional
    public DeactivateStoreResponseDto deactivateStoreById(Long id, DeactivateStoreRequestDto dto) {

        if (!storeRepository.existsById(id)) {
            throw new EntityNotFoundException("가게를 찾을 수 없습니다.");
        }

        storeRepository.deleteById(id);
        Store findStore = storeRepository.findByIdOrElseThrow(id);

        findStore.deactivate();
        return new DeactivateStoreResponseDto(findStore);
    }

}
