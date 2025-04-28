package com.example.yamyam16.store.controller;

import com.example.yamyam16.auth.common.consts.Const;
import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.menu.dto.MenuListResponseDto;
import com.example.yamyam16.menu.service.MenuService;
import com.example.yamyam16.store.dto.request.CreateStoreRequestDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;
import com.example.yamyam16.store.dto.response.CreateStoreResponseDto;
import com.example.yamyam16.store.dto.response.DeactivateStoreResponseDto;
import com.example.yamyam16.store.dto.response.SearchStoreResponseDto;
import com.example.yamyam16.store.dto.response.UpdateStoreResponseDto;
import com.example.yamyam16.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final MenuService menuService;

    //가게생성
    @PostMapping("/owner")
    public ResponseEntity<CreateStoreResponseDto> createStore(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @Valid @RequestBody CreateStoreRequestDto dto
    ) {

        CreateStoreResponseDto createPostsResponseDto = storeService.createStore(dto, user);

        return new ResponseEntity<>(createPostsResponseDto, HttpStatus.CREATED);
    }


    //가게 자기 소유 조회
    @GetMapping("/owner")
    public ResponseEntity<Page<SearchStoreResponseDto>> getAllStoresByOwner(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        return ResponseEntity.ok(storeService.getAllStoresByOwner(user, page, size));

    }


    //가게 전체 조회
    @GetMapping("all")
    public ResponseEntity<Page<SearchStoreResponseDto>> getAllStores(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(storeService.getAllStores(page, size));

    }

    //가게 단건 조회
    @GetMapping("/search/{storeId}/menu")
    public ResponseEntity<List<MenuListResponseDto>> findMenuByPage(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") Long page, // 페이지 번호
            @RequestParam(defaultValue = "10") Long size // 한페이지에 몇개의 데이터를 가져올지
    ) {//url 파라미터로 페이지 번호, 크기 받기
        List<MenuListResponseDto> menuList = menuService.findMenuByPage(storeId, page, size);
        return new ResponseEntity<>(menuList, HttpStatus.OK);
    }

    //가게 전체 이름으로 조회-> filtering 개념으로 requestParam dto로 구현 X
    @GetMapping("/search")
    public ResponseEntity<Page<SearchStoreResponseDto>> getSearchStores(
//            @RequestParam("search") String storename,
            @RequestParam String storename,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(storeService.getAllStoresByName(storename, page, size));

    }

    //추천검색
    

    //가게수정
    @PatchMapping("/owner/{storeId}")
    public ResponseEntity<UpdateStoreResponseDto> updateStoreById(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @PathVariable Long storeId,
            @Valid @RequestBody UpdateStoreRequestDto dto
    ) {

        UpdateStoreResponseDto updateStoreResponseDto = storeService.updateStoreById(storeId, dto, user);
        return new ResponseEntity<>(updateStoreResponseDto, HttpStatus.OK);
    }


    // 가게삭제
    @DeleteMapping("/owner/{storeId}")
    public ResponseEntity<DeactivateStoreResponseDto> deactivateStore(
            @SessionAttribute(name = Const.LOGIN_USER) User user,
            @PathVariable Long storeId
    ) {

        DeactivateStoreResponseDto deactivateUserResponseDto = storeService.deactivateStoreById(storeId, user);

        return new ResponseEntity<>(deactivateUserResponseDto, HttpStatus.OK);

    }

}
