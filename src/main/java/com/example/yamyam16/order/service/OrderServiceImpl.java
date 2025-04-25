package com.example.yamyam16.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.yamyam16.cart.entity.Cart;
import com.example.yamyam16.cart.enums.CartStatus;
import com.example.yamyam16.cart.repository.CartRepository;
import com.example.yamyam16.cart.service.CartService;
import com.example.yamyam16.menu.repository.MenuRepository;
import com.example.yamyam16.order.dto.request.ChangeOrderStatusRequestDto;
import com.example.yamyam16.order.dto.response.ChangeOrderStatusResponseDto;
import com.example.yamyam16.order.dto.response.MenuItemDto;
import com.example.yamyam16.order.dto.response.OwnerOrderResponseDto;
import com.example.yamyam16.order.dto.response.UserOrderResponseDto;
import com.example.yamyam16.order.entity.Order;
import com.example.yamyam16.order.enums.OrderStatus;
import com.example.yamyam16.order.repository.OrderRepository;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;

@Service
public class OrderServiceImpl implements OrderService {

	private static CartRepository cartRepository;
	private static MenuRepository menuRepository;
	private static StoreRepository storeRepository;
	private static OrderRepository orderRepository;
	private static CartService cartService;

	@Override
	public UserOrderResponseDto save(Long userId) {

		// 유저의 모든 장바구니 조회 (인카트인것만)
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		// 비었다면?
		if (userCarts.isEmpty()) {
			throw new RuntimeException("장바구니가 비어있습니다.");
		}

		// 카트 - 해당 스토어 조회
		Store findStore = userCarts.get(0).getMenu().getStore();
		// 주문시 전체 금액 조회
		Long totalPrice = userCarts.stream()
			.mapToLong(cart -> cart.getMenu().getMenuPrice() * cart.getQuantity())
			.sum();

		// 주문 생성 - 상태는 ordered
		Order order = new Order(userId, findStore.getId(), totalPrice, OrderStatus.ORDERED, LocalDateTime.now());
		orderRepository.save(order);

		// 카트 상태 변경
		for (Cart cart : userCarts) {
			cart.setStatus(CartStatus.ORDERED);
		}
		// 리스트 만들기 -> Menuitemdto 사용
		List<MenuItemDto> menuItems = userCarts.stream()
			.map(cart -> new MenuItemDto(cart.getMenu().getMenuName(), cart.getQuantity()))
			.collect(
				Collectors.toList());

		// List<MenuItemDto> menuItems = new ArrayList<>();
		// for (Cart cart : userCarts) {
		// 	menuItems.add(new MenuItemDto(cart.getMenu().getName(), cart.getQuantity()));
		// }

		return new UserOrderResponseDto(order.getOrderId(), findStore.getName(), totalPrice, order.getStatus().name(),
			order.getOrderedAt());
	}

	@Override
	public OwnerOrderResponseDto accept(Long userId, Long orderId) {
		return null;
	}

	@Override
	public ChangeOrderStatusResponseDto changeStatus(Long userId, Long orderId,
		ChangeOrderStatusRequestDto statusRequestDto) {
		return null;
	}

	@Override
	public void cancleOrder(Long userId, Long orderId, ChangeOrderStatusRequestDto statusRequestDto) {

	}
}
