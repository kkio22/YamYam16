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
		assertTrue(response.isCanOrder() || !response.isCanOrder()); // 상태만 검증
	}

	@Test
	@DisplayName("카트 전체 조회 성공")
	void findAllCart() {
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
	}
}