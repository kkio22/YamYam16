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

//	@GetMapping("/stores/{storeId}/menu")
//	public ResponseEntity<List<MenuListResponseDto>> findMenuByPage(
//		@PathVariable Long storeId,
//		@RequestParam(defaultValue = "1") Long page, // 페이지 번호
//		@RequestParam(defaultValue = "10") Long size // 한페이지에 몇개의 데이터를 가져올지
//	) {//url 파라미터로 페이지 번호, 크기 받기
//		List<MenuListResponseDto> menuList = menuService.findMenuByPage(storeId, page, size);
//		return new ResponseEntity<>(menuList, HttpStatus.OK);
//	}

    @PatchMapping("/stores/{storeId}/menu/{menuId}")// url이 달라야 함,
    public ResponseEntity<MenuUpdateResponseDto> updateMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequestDto menuUpdateRequestDto
    ) {
        MenuUpdateResponseDto menuUpdateResponseDto = menuService.updateMenu(storeId, menuId, menuUpdateRequestDto);
        return new ResponseEntity<>(menuUpdateResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/stores/{storeId}/menu/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ) {
        menuService.deleteMenu(storeId, menuId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}