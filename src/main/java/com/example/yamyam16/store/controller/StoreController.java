package com.example.yamyam16.store.controller;

import com.example.yamyam16.auth.common.consts.Const;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.service.UserService;
import com.example.yamyam16.store.dto.request.CreateStoreRequestDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;
import com.example.yamyam16.store.dto.response.*;
import com.example.yamyam16.store.entity.enums.CategoryType;
import com.example.yamyam16.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    //가게생성
    @PostMapping
    public ResponseEntity<CreateStoreResponseDto> createStore(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @Valid @RequestBody CreateStoreRequestDto dto
    ) {

        CreateStoreResponseDto createPostsResponseDto = storeService.createStore(dto, user);

        return new ResponseEntity<>(createPostsResponseDto, HttpStatus.CREATED);
    }

    //가게 전체 조회 및 이름으로 조회-> filtering 개념으로 requestParam dto로 구현 X

    @GetMapping("/{storeName}")
    public ResponseEntity<Page<SearchStoreResponseDto>> getAllStores(
            @RequestParam(required = false) String storeName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(storeService.getAllStores(storeName, pageable));

    }

    //가게 카테고리별 조회

    @GetMapping("/{storeId}")
    public ResponseEntity<Page<SearchStoreResponseDto>> getStoresByCategory(
            @RequestParam(required = false) String storeName,
            @PathVariable CategoryType categorytype
    ) {

        return ResponseEntity.of()
    }

    //가게 단일 조회

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<MenuListResponseDto>> findMenuByPage(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") Long page, // 페이지 번호
            @RequestParam(defaultValue = "10") Long size // 한페이지에 몇개의 데이터를 가져올지
    ) {//url 파라미터로 페이지 번호, 크기 받기
        List<MenuListResponseDto> menuList = storeService.findMenuByPage(storeId, page, size);
        return new ResponseEntity<>(menuList, HttpStatus.OK);
    }


    //가게수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<UpdateStoreResponseDto> updateStoreById(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @PathVariable Long storeId,
            @Valid @RequestBody UpdateStoreRequestDto dto
    ) {

        UpdateStoreResponseDto updateStoreResponseDto = storeService.updateStoreById(storeId, dto, user);
        return new ResponseEntity<>(updateStoreResponseDto, HttpStatus.OK);
    }

    //가게 공지 수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<UpdateStoreResponseDto> updateStoreById(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
    ){

    }

    // 가게삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<DeactivateStoreResponseDto> deactivateStore(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @PathVariable Long storeId
    ) {

        DeactivateStoreResponseDto deactivateUserResponseDto = storeService.deactivateStoreById(storeId, user);

        return new ResponseEntity<>(deactivateUserResponseDto, HttpStatus.OK);

    }

}
