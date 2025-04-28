package com.example.yamyam16.cart.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.yamyam16.cart.enums.CartStatus;
import com.example.yamyam16.cart.repository.CartRepository;
import com.example.yamyam16.menu.MenuStatus;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.menu.repository.MenuRepository;
import com.example.yamyam16.store.entity.CategoryType;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;
import com.example.yamyam16.cart.dto.request.CartRequestDto;
import com.example.yamyam16.cart.dto.request.DeleteCartRequestDto;
import com.example.yamyam16.cart.dto.request.UpdateCartRequestDto;
import com.example.yamyam16.cart.dto.response.SaveCartResponseDto;
import com.example.yamyam16.cart.dto.response.FindAllCartResponseDto;
import com.example.yamyam16.cart.entity.Cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.DeleteMapping;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

	@Mock
	private CartRepository cartRepository;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private CartServiceImpl cartService;

	@Test
	@DisplayName("카트 저장 성공")
	void saveCart() {
		// 가게와 메뉴를 찾고, 기존 장바구니 비어있을 때 새 메뉴 담기 성공하는지 검증
		// given
		Long storeId = 1L;
		Long userId = 1L;
		CartRequestDto requestDto = new CartRequestDto("Burger", 2L);

		Store store = Store.builder()
			.name("Test Store")
			.openTime(9L)
			.closeTime(22L)
			.minOrderPrice(10000L)
			.category(CategoryType.Westerncuisine)
			.build();

		Menu menu = Menu.builder()
			.menuName("Burger")
			.menuPrice(5000L)
			.menuStatus(MenuStatus.AVAILABLE)
			.build();
		menu.setStore(store);

		when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
		when(menuRepository.findByMenuNameAndStore_Id("Burger", storeId)).thenReturn(menu);
		when(cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART)).thenReturn(Collections.emptyList());

		// when
		SaveCartResponseDto response = cartService.save(storeId, userId, requestDto);

		// then
		assertThat(response.getMessage()).contains("추가되었습니다");
		assertTrue(response.isCanOrder()); // 최소 주문 조건 만족
		// assertThat 실제 결과가 기대한 조건을 만족하는지” 확인하는 검증 구문
		// response.getMessage()가 가져온 문자열 안에
		// "추가되었습니다"라는 문구가 포함되어 있는지 검사
		// assertTrue 괄호 안 조건이 true여야 테스트가 성공
	}

	@Test
	@DisplayName("카트 전체 조회 성공")
	void findAllCart() {
		// 유저의 장바구니 목록을 불러오고, 메시지가 제대로 오는지 확인
		// given
		Long userId = 1L;
		Long storeId = 1L;

		Store store = Store.builder()
			.name("Test Store")
			.openTime(9L)
			.closeTime(22L)
			.minOrderPrice(10000L)
			.category(CategoryType.Westerncuisine)
			.build();

		Menu menu = Menu.builder()
			.menuName("Burger")
			.menuPrice(5000L)
			.menuStatus(MenuStatus.AVAILABLE)
			.build();
		menu.setStore(store);

		Cart cart = new Cart(userId, menu, 2L);

		when(cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART)).thenReturn(List.of(cart));
		when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

		// when
		FindAllCartResponseDto response = cartService.findAll(userId, storeId);

		// then
		assertThat(response.getMessage()).contains("장바구니 목록");
	}

	@Test
	@DisplayName("카트 수량 수정 성공")
	void updateCart() {
		// 기존 카트 항목에서 수량을 수정한 뒤, 수정된 수량과 메시지를 검증
		// given
		Long userId = 1L;

		UpdateCartRequestDto requestDto = new UpdateCartRequestDto();
		ReflectionTestUtils.setField(requestDto,"menuName","Burger");
		ReflectionTestUtils.setField(requestDto,"quantity",3L);
		// UpdateCartRequestDto requestDto = new UpdateCartRequestDto("Burger", 3L);

		Store store = Store.builder()
			.name("Test Store")
			.openTime(9L)
			.closeTime(22L)
			.minOrderPrice(10000L)
			.category(CategoryType.Westerncuisine)
			.build();

		Menu menu = Menu.builder()
			.menuName("Burger")
			.menuPrice(5000L)
			.menuStatus(MenuStatus.AVAILABLE)
			.build();
		menu.setStore(store);

		Cart cart = new Cart(userId, menu, 2L);

		when(cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART)).thenReturn(List.of(cart));

		// when
		FindAllCartResponseDto response = cartService.update(userId, requestDto);

		// then
		assertThat(cart.getQuantity()).isEqualTo(3L);
		assertThat(response.getMessage()).contains("수량이 수정되었습니다");
	}

	@Test
	@DisplayName("카트 항목 삭제 성공")
	void deleteCart() {
		// 장바구니에서 특정 메뉴를 삭제하고, 삭제 메시지가 제대로 뜨는지 확인
		// given
		Long userId = 1L;
		// DeleteCartRequestDto requestDto = new DeleteCartRequestDto("Burger");
		DeleteCartRequestDto requestDto = new DeleteCartRequestDto();
		ReflectionTestUtils.setField(requestDto,"menuName","Burger");

		Store store = Store.builder()
			.name("Test Store")
			.openTime(9L)
			.closeTime(22L)
			.minOrderPrice(10000L)
			.category(CategoryType.Westerncuisine)
			.build();

		Menu menu = Menu.builder()
			.menuName("Burger")
			.menuPrice(5000L)
			.menuStatus(MenuStatus.AVAILABLE)
			.build();
		menu.setStore(store);

		Cart cart = new Cart(userId, menu, 2L);

		when(cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART)).thenReturn(List.of(cart));

		// when
		FindAllCartResponseDto response = cartService.delete(userId, requestDto);

		// then
		assertThat(response.getMessage()).contains("삭제되었습니다");
	}

	@Test
	@DisplayName("모든 카트 삭제 성공")
	void deleteAllCart() {
		// 유저의 모든 장바구니 항목을 삭제하고, deleteAll 메소드가 호출됐는지 검증
		// given
		Long userId = 1L;

		Menu menu = Menu.builder()
			.menuName("Burger")
			.menuPrice(5000L)
			.menuStatus(MenuStatus.AVAILABLE)
			.build();
		Store store = Store.builder()
			.name("Test Store")
			.openTime(9L)
			.closeTime(22L)
			.minOrderPrice(10000L)
			.category(CategoryType.Westerncuisine)
			.build();
		menu.setStore(store);

		Cart cart = new Cart(userId, menu, 2L);

		when(cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART)).thenReturn(List.of(cart));

		// when
		cartService.deleteAll(userId);

		// then
		verify(cartRepository, times(1)).deleteAll(anyList());
		// verfiy() Mockito 기능, Mock 객체에서 특정 메소드가 실제로 호출 됐는지 확인
		// 1.	cartRepository.deleteAll(…) 이라는 메소드가
		// 2.	정확히 1번 호출됐는지 (times(1))
		// 3. 그리고 deleteAll 메소드의 인자로 어떤 리스트(anyList()) 가 들어갔는지 체크
	}
}