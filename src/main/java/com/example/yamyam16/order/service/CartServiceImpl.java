package com.example.yamyam16.order.service;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.repository.UserRepository;
import com.example.yamyam16.order.dto.request.CartRequestDto;
import com.example.yamyam16.order.dto.response.CartItemDto;
import com.example.yamyam16.order.dto.response.SaveCartResponseDto;
import com.example.yamyam16.order.entity.Cart;
import com.example.yamyam16.order.enums.CartStatus;
import com.example.yamyam16.order.repository.CartRepository;

public class CartServiceImpl implements CartService {

	private static CartRepository cartRepository;
	private static UserRepository userRepository;
	private static MenuRepository menuRepository;

	@Override
	public SaveCartResponseDto save(Long storeId, Long userId, CartRequestDto requestDto) {

		// 가게 찾기
		Store findStore = storeRepository.findByIdOrElseThrow(storeId);
		// 유저 찾기
		User findUser = userRepository.findByIdOrElseThrow(userId);
		// 메뉴 찾기 (가게 메뉴에서 금액 가져와야함)
		Menu findMenu = menuRepository.findByNameAndStoreId(requestDto.getMenu(), storeId)

		// 필요한것 ( 카트 아이디, 메뉴, 수량, 가격 )
		Cart cart = new Cart(findUser, findMenu, requestDto.getQuantity());
		cartRepository.save(cart);

		// 유저의 다른 카트들 조회
		List<Cart> userCarts = cartRepository.findByUserAndStatus(findUser, CartStatus.IN_CART);
		// 같은 스토어 아이디에서만 담을 수 있음
		if (!userCarts.isEmpty()) {
			Store store = userCarts.get(0).getMenu().getStore();
			if(!store.getId().equals(findStore.getId())){
				throw new RuntimeException("같은 가게의 음식만 담을 수 있습니다.");
			}
		}

		return SaveCartResponseDto.toDto(cart, userCarts, findStore.getMinPrice());
	}
}
