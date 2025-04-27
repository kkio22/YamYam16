package com.example.yamyam16.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yamyam16.auth.repository.UserRepository;
import com.example.yamyam16.cart.dto.request.CartRequestDto;
import com.example.yamyam16.cart.dto.request.DeleteCartRequestDto;
import com.example.yamyam16.cart.dto.request.UpdateCartRequestDto;
import com.example.yamyam16.cart.dto.response.FindAllCartResponseDto;
import com.example.yamyam16.cart.dto.response.SaveCartResponseDto;
import com.example.yamyam16.cart.entity.Cart;
import com.example.yamyam16.cart.enums.CartStatus;
import com.example.yamyam16.cart.exception.DifferentStoreCartException;
import com.example.yamyam16.cart.repository.CartRepository;
import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.exception.ErrorCode;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.menu.repository.MenuRepository;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;

	@Override
	public SaveCartResponseDto save(Long storeId, Long userId, CartRequestDto requestDto) {

		// 가게 찾기
		Store findStore = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		// 메뉴 찾기 (가게 메뉴에서 금액 가져와야함)
		Menu findMenu = menuRepository.findByMenuNameAndStore_Id(requestDto.getMenuName(), storeId);

		if (findMenu == null) {
			throw new CustomException(ErrorCode.MENU_NOT_FOUND);
		}

		// 해당 유저의 다른 카트들 조회
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		// 같은 스토어에서만 담을 수 있음
		if (!userCarts.isEmpty()) {
			if (!userCarts.get(0).getMenu().getStore().getId().equals(storeId)) {
				throw new DifferentStoreCartException();
			}
		}

		// 필요한것 ( 카트 아이디, 메뉴, 수량, 가격 )
		Cart cart = new Cart(userId, findMenu, requestDto.getQuantity());
		cartRepository.save(cart);

		String message = requestDto.getMenuName() + "이 추가되었습니다.";
		return SaveCartResponseDto.toDto(message, cart, userCarts, findStore.getMinOrderPrice());
	}

	@Override
	public FindAllCartResponseDto findAll(Long userId, Long storeId) {
		// 카트목록을 리스트로
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		// 스토어의 최소 주문금액
		Store findStore = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException());

		String message = userCarts.isEmpty() ? "장바구니가 비었습니다" : "현재 전체 장바구니 목록";
		return FindAllCartResponseDto.toDto(message, userCarts, findStore.getMinOrderPrice());
	}

	@Transactional
	@Override
	public FindAllCartResponseDto update(Long userId, UpdateCartRequestDto requestDto) {
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		Cart findCart = userCarts.stream()
			.filter(cart -> cart.getMenu().getMenuName().equals(requestDto.getMenuName()))
			.findFirst().orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

		if (requestDto.getQuantity() == 0) {
			cartRepository.delete(findCart);
		} else {
			findCart.update(requestDto.getQuantity());
		}

		// 카트목록을 리스트로
		userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		Store findStore = findCart.getMenu().getStore();

		String message = requestDto.getQuantity() == 0 ? "이 삭제되었습니다." : "의 수량이 수정되었습니다.";

		return FindAllCartResponseDto.toDto(requestDto.getMenuName() + message, userCarts,
			findStore.getMinOrderPrice());
	}

	@Transactional
	@Override
	public FindAllCartResponseDto delete(Long userId, DeleteCartRequestDto requestDto) {
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		Cart findCart = userCarts.stream()
			.filter(cart -> cart.getMenu().getMenuName().equals(requestDto.getMenuName()))
			.findFirst().orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

		cartRepository.delete(findCart);

		// 카트목록을 리스트로
		userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		Store findStore = findCart.getMenu().getStore();

		String message = requestDto.getMenuName() + "가 삭제되었습니다.";

		return FindAllCartResponseDto.toDto(message, userCarts, findStore.getMinOrderPrice());
	}

	@Override
	public void deleteAll(Long userId) {
		// 유저의 모든 카트 찾기
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);
		cartRepository.deleteAll(userCarts);

	}

}
