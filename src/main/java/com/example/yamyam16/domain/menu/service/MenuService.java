package com.example.yamyam16.domain.menu.service;

import org.springframework.stereotype.Service;

import com.example.yamyam16.domain.menu.dto.MenuCreateRequestDto;
import com.example.yamyam16.domain.menu.dto.MenuCreateResponseDto;
import com.example.yamyam16.domain.menu.dto.MenuUpdateRequestDto;
import com.example.yamyam16.domain.menu.dto.MenuUpdateResponseDto;
import com.example.yamyam16.domain.menu.entity.Menu;
import com.example.yamyam16.domain.menu.repository.MenuRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

	public final MenuRepository menuRepository;
	public final StoreRepository storeRepository;

	public MenuCreateResponseDto createMenu(
		Long storeId,
		MenuCreateRequestDto menuCreateRequestDto
	) {
		Store findStore = storeRepository.findById(storeId)
			.orElseThrow(() -> new storeNotFoundException("해당 가게가 없습니다.")); //해당 가게가 있는지 확인

		Menu menu = new Menu(
			menuCreateRequestDto.getMenuName(),
			menuCreateRequestDto.getMenuPrice()
		); // dto -> entiity로 변환 entity가 자바 클래스이니 객체로 생성해서 값을 보내서 해당 값으로 바꾼 Menu 클래스를 생성

		Menu savedMenu = menuRepository.save(menu); //repository에 엔티티 보내서 데이터 베이스 테이블에 값 생성하고 그 값이 다시 반환됨 그래서 자료형이 Menu
		return new MenuCreateResponseDto(savedMenu.getId(), savedMenu.getMenuName(), savedMenu.getMenuPrice());
	}

	public MenuUpdateResponseDto updateMenu(
		Long storeId,
		Long menuId,
		MenuUpdateRequestDto menuUpdateRequestDto
	) {
		Store findStore = storeRepository.findById(storeId)
			.orElseThrow(() -> new storeNotFoundException("해당 가게가 없습니다."));
		Menu findMenu = MenuRepository.findById(menuId).orElseThrow(() -> new menuNotFooundException("해당 메뉴가 없습니다"));
		Menu updateMenu = MenuRepository.save(menuUpdateRequestDto.getMenuName(), menuUpdateRequestDto.getMenuPrice());
		return new MenuUpdateResponseDto(updateMenu.getId(), updateMenu.getMenuName(), updateMenu.getMenuPrice())
	}

	@Transactional
	public void deleteMenu( //softDelete
		Long storeId,
		Long menuId
	) {
		Store findStore = storeRepository.findById(storeId)
			.orElseThrow(() -> new storeNotFoundException("해당 가게가 없습니다."));

		Menu findMenu = MenuRepository.findById(menuId).orElseThrow(() -> new menuNotFooundException("해당 메뉴가 없습니다"));

		findMenu.deleteMenu(); // 가게 메뉴는 소프트삭제가 됨
	}

}
