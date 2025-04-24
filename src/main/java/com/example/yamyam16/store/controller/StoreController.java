package com.example.yamyam16.store.controller;

import com.example.yamyam16.auth.service.UserService;
import com.example.yamyam16.store.dto.request.CreateStoreRequestDto;
import com.example.yamyam16.store.dto.request.UpdateStoreRequestDto;
import com.example.yamyam16.store.dto.response.UpdateStoreResponseDto;
import com.example.yamyam16.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/signup")
    public ResponseEntity<Void> createStore(@Valid @RequestBody CreateStoreRequestDto dto) {


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //가게조회
    @PostMapping("/profile")
    public ResponseEntity<UpdateStoreResponseDto> createProfile(Long id, @Valid @RequestBody UpdateStoreRequestDto dto) {

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //가게수정
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword() {


        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 가게삭제
    @PatchMapping("/{storeid}")
    public ResponseEntity<DeactivateStoreResponseDto> deactivateStore() {


        return new ResponseEntity<>(HttpStatus.OK);
    }


}
