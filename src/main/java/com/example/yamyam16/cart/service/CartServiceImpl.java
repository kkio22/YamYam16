package com.example.yamyam16.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.yamyam16.auth.repository.UserRepository;
import com.example.yamyam16.cart.dto.request.CartRequestDto;
import com.example.yamyam16.cart.dto.request.UpdateCartRequestDto;
import com.example.yamyam16.cart.dto.response.FindAllCartResponseDto;
import com.example.yamyam16.cart.dto.response.SaveCartResponseDto;
import com.example.yamyam16.cart.entity.Cart;
import com.example.yamyam16.cart.enums.CartStatus;
import com.example.yamyam16.cart.exception.DifferentStoreCartException;
import com.example.yamyam16.cart.exception.UnauthorizedCartDeleteException;
import com.example.yamyam16.cart.exception.UnauthorizedCartModificationException;
import com.example.yamyam16.cart.repository.CartRepository;
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
		Store findStore = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException());

		// 메뉴 찾기 (가게 메뉴에서 금액 가져와야함)
		Menu findMenu = menuRepository.findByMenuNameAndStore_Id(requestDto.getMenuName(), storeId);

		if (findMenu == null) {
			throw new RuntimeException("메뉴를 찾을 수 없습니다");
		}

		// 해당 유저의 다른 카트들 조회
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		// 같은 스토어에서만 담을 수 있음
		if (!userCarts.isEmpty()) {
			Store store = findMenu.getStore();
			if (!store.getId().equals(findStore.getId())) {
				throw new DifferentStoreCartException();
			}
		}

		// 필요한것 ( 카트 아이디, 메뉴, 수량, 가격 )
		Cart cart = new Cart(userId, findMenu, requestDto.getQuantity());
		cartRepository.save(cart);

		return SaveCartResponseDto.toDto(cart, userCarts, findStore.getMinOrderPrice());
	}

	@Override
	public FindAllCartResponseDto findAll(Long userId, Long storeId) {
		// 카트목록을 리스트로
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		// 스토어의 최소 주문금액
		Store findStore = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException());

		return FindAllCartResponseDto.toDto(userCarts, findStore.getMinOrderPrice());
	}

	@Transactional
	@Override
	public FindAllCartResponseDto update(Long userId, Long cartId, UpdateCartRequestDto requestDto) {
		Cart findCart = cartRepository.findByIdOrElseThrow(cartId);

		if (!findCart.getUserId().equals(userId)) {
			throw new UnauthorizedCartDeleteException();
		}

		if (requestDto.getQuantity() == 0) {
			cartRepository.delete(findCart);
		} else {
			findCart.update(requestDto.getQuantity());
		}

		// 카트목록을 리스트로
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);
		Store findStore = findCart.getMenu().getStore();

		return FindAllCartResponseDto.toDto(userCarts, findStore.getMinOrderPrice());
	}

	@Transactional
	@Override
	public FindAllCartResponseDto delete(Long userId, Long cartId) {
		Cart findCart = cartRepository.findByIdOrElseThrow(cartId);

		if (!findCart.getUserId().equals(userId)) {
			throw new UnauthorizedCartModificationException();
		}

		cartRepository.delete(findCart);

		// 카트목록을 리스트로
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);
		Store findStore = findCart.getMenu().getStore();

		return FindAllCartResponseDto.toDto(userCarts, findStore.getMinOrderPrice());
	}

	@Override
	public void deleteAll(Long userId) {
		// 유저의 모든 카트 찾기
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);
		cartRepository.deleteAll(userCarts);

	}

}
