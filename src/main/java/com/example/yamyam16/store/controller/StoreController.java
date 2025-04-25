package com.example.yamyam16.store.controller;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.service.UserService;
import com.example.yamyam16.store.dto.request.CreateStoreRequestDto;
import com.example.yamyam16.store.dto.request.DeactivateStoreRequestDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;
import com.example.yamyam16.store.dto.response.CreateStoreResponseDto;
import com.example.yamyam16.store.dto.response.DeactivateStoreResponseDto;
import com.example.yamyam16.store.dto.response.SearchStoreResponseDto;
import com.example.yamyam16.store.dto.response.UpdateStoreResponseDto;
import com.example.yamyam16.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    //가게생성
    @PostMapping
    public ResponseEntity<Void> createStore(
            @Valid @RequestBody CreateStoreRequestDto dto,
            User user
    ) {
        CreateStoreResponseDto createPostsResponseDto = storeService.createStore(dto, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //가게 조회
    @GetMapping("/allstores")
    public ResponseEntity<Page<SearchStoreResponseDto>> getAllStores(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        //todo 추가함 확인필요
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(storeService.getAllStores(pageable));

    }

    //가게 카테고리별 조회
//    @GetMapping("/allstores/{category}")
//    public ResponseEntity<Page<SearchStoreResponseDto>> getStoresByCategory(
//            @Valid @RequestBody SearchStoreRequestDto dto,
//            @PathVariable CategoryType categorytype
//    ) {
//
//    }


    //가게수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<UpdateStoreResponseDto> updateStoreById(
            @PathVariable Long storeId,
            @Valid @RequestBody UpdateStoreRequestDto dto
    ) {
        UpdateStoreResponseDto updateStoreResponseDto = storeService.updateStoreById(storeId, dto);
        return new ResponseEntity<>(updateStoreResponseDto, HttpStatus.OK);
    }


    // 가게삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<DeactivateStoreResponseDto> deactivateStore(
            @PathVariable Long storeId,
            DeactivateStoreRequestDto dto
    ) {
        DeactivateStoreResponseDto deactivateUserResponseDto = storeService.deactivateStoreById(storeId, dto);

        return new ResponseEntity<>(deactivateUserResponseDto, HttpStatus.OK);

    }

}
