package com.example.yamyam16.menu.controller;

import com.example.yamyam16.menu.dto.MenuCreateRequestDto;
import com.example.yamyam16.menu.dto.MenuCreateResponseDto;
import com.example.yamyam16.menu.dto.MenuUpdateRequestDto;
import com.example.yamyam16.menu.dto.MenuUpdateResponseDto;
import com.example.yamyam16.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MenuController {

    public final MenuService menuService;

    @PostMapping("/stores/{storeId}/menu")
    public ResponseEntity<MenuCreateResponseDto> createMenu( //매서드명은 동사가 먼저와야 함
                                                             @PathVariable Long storeId, //지금 연관 관계된 가게의 url에 데이터 베이스 id 값이 있기에 그걸로 연관 지으면 됨
                                                             @Valid @RequestBody MenuCreateRequestDto menuCreateRequestDto
    ) {
        MenuCreateResponseDto menuCreateResponseDto = menuService.createMenu(storeId, menuCreateRequestDto); //dto
        return new ResponseEntity<>(menuCreateResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/stores/{storeId}/menu/{menuId}/edit")// url이 달라야 함
    public ResponseEntity<MenuUpdateResponseDto> updateMenu(
            @PathVariable Long storeId,
            Long menuId,
            @RequestBody MenuUpdateRequestDto menuUpdateRequestDto
    ) {
        MenuUpdateResponseDto menuUpdateResponseDto = menuService.updateMenu(storeId, menuId, menuUpdateRequestDto);
        return new ResponseEntity<>(menuUpdateResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/store/{storeId}/menu/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long storeId,
            Long menuId
    ) {
        menuService.deleteMenu(storeId, menuId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
