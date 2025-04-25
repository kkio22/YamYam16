package com.example.yamyam16.order.service;

import java.time.LocalDateTime;
import java.util.List;

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
		List<MenuItemDto> menuItems = userCarts.stream().map(MenuItemDto::toDto).toList();

		return new UserOrderResponseDto(order.getOrderId(), findStore.getName(), totalPrice, order.getStatus().name(),
			order.getOrderedAt());
	}

	@Override
	public OwnerOrderResponseDto accept(Long userId, Long orderId) {
		// 주문 조회
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("해당 주문이 존재하지 않습니다."));

		// 로그인 유저가 해당 가게의 사장님이 맞는지 조회
		Store store = storeRepository.findByIdOrElseThrow(userId);
		if (!store.getUser().getId().equals(userId)) {
			throw new RuntimeException("가게 주인만 수락할 수 있습니다");
		}

		// 주문 상태 설정
		order.setStatus(OrderStatus.PREPARING);

		// 리스트 만들기 -> Menuitemdto 사용
		List<MenuItemDto> menuItems = order.getCarts().stream().map(MenuItemDto::toDto).toList();

		return new OwnerOrderResponseDto(order.getOrderId(), menuItems, order.getTotalPrice(), order.getStatus().name(),
			order.getOrderedAt());
	}

	@Override
	public ChangeOrderStatusResponseDto changeStatus(Long userId, Long orderId,
		ChangeOrderStatusRequestDto statusRequestDto) {

		// 주문 조회
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("해당 주문이 존재하지 않습니다."));

		// 상태 변경
		order.setStatus(statusRequestDto.getStatus());

		return new ChangeOrderStatusResponseDto(order.getOrderId(), order.getStatus().name(), order.getOrderedAt());
	}

	@Override
	public void cancleOrder(Long userId, Long orderId, Long loginUserId) {
		// 로그인 유저 = Path의 유저인지 확인
		if (!userId.equals(loginUserId)) {
			throw new RuntimeException("본인의 주문만 취소할 수 있습니다");
		}

		// 주문 조회
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("해당 주문이 존재하지 않습니다."));

		// 주문자 = 유저인지 확인
		if (!order.getUserId().equals(loginUserId)) {
			throw new RuntimeException("본인의 주문만 취소할 수 있습니다");
		}

		// 이미 취소라면?
		if (order.getStatus() == OrderStatus.CANCELED) {
			throw new RuntimeException("이미 취소된 주문입니다.");
		}

		// 주문 상태 확인
		if (order.getStatus() != OrderStatus.ORDERED) {
			throw new RuntimeException("주문이 수락되어 취소할 수 없습니다.");
		}

		// 상태 변경 -> 취소완료
		order.setStatus(OrderStatus.CANCELED);

	}
}
