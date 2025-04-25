package com.example.yamyam16.menu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;
import com.example.yamyam16.menu.dto.MenuCreateRequestDto;
import com.example.yamyam16.menu.dto.MenuCreateResponseDto;
import com.example.yamyam16.menu.dto.MenuListResponseDto;
import com.example.yamyam16.menu.dto.MenuUpdateRequestDto;
import com.example.yamyam16.menu.dto.MenuUpdateResponseDto;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.menu.repository.MenuRepository;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;

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
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND)); //해당 가게가 있는지 확인

		Menu menu = new Menu(
			menuCreateRequestDto.getMenuName(),
			menuCreateRequestDto.getMenuPrice()
		); // dto -> entiity로 변환 entity가 자바 클래스이니 객체로 생성해서 값을 보내서 해당 값으로 바꾼 Menu 클래스를 생성

		Menu savedMenu = menuRepository.save(menu); //repository에 엔티티 보내서 데이터 베이스 테이블에 값 생성하고 그 값이 다시 반환됨 그래서 자료형이 Menu
		return new MenuCreateResponseDto(savedMenu.getId(), savedMenu.getMenuName(), savedMenu.getMenuPrice());
	}

	public List<MenuListResponseDto> findMenuByPage(Long storeId, Long page, Long size) {
		Store findStore = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException((ErrorCode.STORE_NOT_FOUND))); // 해당 가게가 있는지 확인

		//pageable 객체 생성 => 페이지 번호, 페이지 갯수, 정렬 나타냄
		Pageable pageable = PageRequest.of(page.intValue() - 1, size.intValue(),
			Sort.by("menuPrice").ascending()); // 데이터 베이스 기준으로 페이지 번호는 0부터 시작한다. 내가 데이터베이스에서 값을 가져와야  하기에 이 로직 필요

		//가게 id에 대한 메뉴 목록 페이징 조회 (페이징에 대한 정보 + 메뉴 목록)
		Page<Menu> menuPage = menuRepository.findAllByStoreId(storeId,
			pageable); // 데이터 베이스에서 entity 값을 가지고 나오는데 매개변수가 pageable이니 page로 감싼다.

		//메뉴 목록만 조회
		List<Menu> menuList = new ArrayList<>(menuPage.getContent());

		//entity -> dto로 변환해서 반환 => stream이나 for문 사용해야 함
		return menuList.stream() //컬렉션인 List를 스트림으로 변환
			.map(menu -> new MenuListResponseDto(menu.getId(), menu.getMenuName(),//스트릶에 있는 menu객체를 하나씩 꺼내옴
				menu.getMenuPrice())) // entity -> dto로 변경 (MenuListResponseDto :: new)
			.toList(); // 다시 List로 변환
	}

	public MenuUpdateResponseDto updateMenu(
		Long storeId,
		Long menuId,
		MenuUpdateRequestDto menuUpdateRequestDto
	) {
		Store findStore = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		Menu findMenu = menuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

		findMenu.updateMenu(menuUpdateRequestDto.getMenuName(), menuUpdateRequestDto.getMenuPrice());

		return new MenuUpdateResponseDto(findMenu.getId(), findMenu.getMenuName(), findMenu.getMenuPrice());
	}

	@Transactional
	public void deleteMenu( //softDelete
		Long storeId,
		Long menuId
	) {
		Store findStore = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		Menu findMenu = menuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

		findMenu.deleteMenu(); // 가게 메뉴는 소프트삭제가 됨
	}

}
