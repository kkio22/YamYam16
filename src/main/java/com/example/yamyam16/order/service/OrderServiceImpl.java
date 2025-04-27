package com.example.yamyam16.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.entity.UserType;
import com.example.yamyam16.auth.repository.UserRepository;
import com.example.yamyam16.cart.entity.Cart;
import com.example.yamyam16.cart.enums.CartStatus;
import com.example.yamyam16.cart.repository.CartRepository;
import com.example.yamyam16.order.dto.request.ChangeOrderStatusRequestDto;
import com.example.yamyam16.order.dto.response.ChangeOrderStatusResponseDto;
import com.example.yamyam16.order.dto.response.FindAllOrderResponseDto;
import com.example.yamyam16.order.dto.response.MenuItemDto;
import com.example.yamyam16.order.dto.response.OwnerOrderResponseDto;
import com.example.yamyam16.order.dto.response.UserOrderResponseDto;
import com.example.yamyam16.order.entity.Order;
import com.example.yamyam16.order.enums.OrderStatus;
import com.example.yamyam16.order.exception.AlreadyCanceledOrderException;
import com.example.yamyam16.order.exception.EmptyCartOrderException;
import com.example.yamyam16.order.exception.OrderAlreadyAcceptedException;
import com.example.yamyam16.order.exception.UnauthorizedOrderAcceptException;
import com.example.yamyam16.order.exception.UnauthorizedOrderAccessException;
import com.example.yamyam16.order.exception.UnauthorizedOrderCancelException;
import com.example.yamyam16.order.repository.OrderRepository;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;
import com.example.yamyam16.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final CartRepository cartRepository;
	private final StoreRepository storeRepository;
	private final OrderRepository orderRepository;
	private final StoreService storeService;
	private final UserRepository userRepository;

	@Override
	public UserOrderResponseDto save(Long userId) {

		// 유저의 모든 장바구니 조회 (인카트인것만)
		List<Cart> userCarts = cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART);

		// 비었다면?
		if (userCarts.isEmpty()) {
			throw new EmptyCartOrderException();
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
		Order order = orderRepository.findByIdOrElseThrow(orderId);
		// 로그인 유저가 해당 가게의 사장님이 맞는지 조회
		Store store = storeRepository.findByIdOrElseThrow(userId);
		if (!store.getUser().getId().equals(userId)) {
			throw new UnauthorizedOrderAcceptException();
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
		Order order = orderRepository.findByIdOrElseThrow(orderId);
		// 상태 변경
		order.setStatus(statusRequestDto.getStatus());

		// 카트 상태 복구
		if (statusRequestDto.getStatus() == OrderStatus.CANCELED) {
			for (Cart cart : order.getCarts()) {
				cart.setStatus(CartStatus.IN_CART);
			}
		}

		return new ChangeOrderStatusResponseDto(order.getOrderId(), order.getStatus().name(), order.getOrderedAt());
	}

	@Override
	public void cancelOrder(Long orderId, Long loginUserId) {

		// 주문 조회
		Order order = orderRepository.findByIdOrElseThrow(orderId);

		// 주문자 = 유저인지 확인
		if (!order.getUserId().equals(loginUserId)) {
			throw new UnauthorizedOrderCancelException();
		}

		// 이미 취소라면?
		if (order.getStatus() == OrderStatus.CANCELED) {
			throw new AlreadyCanceledOrderException();
		}

		// 주문 상태 확인
		if (order.getStatus() != OrderStatus.ORDERED) {
			throw new OrderAlreadyAcceptedException();
		}

		// 상태 변경 -> 취소완료
		order.setStatus(OrderStatus.CANCELED);
		// 카트 상태 복구
		for (Cart cart : order.getCarts()) {
			cart.setStatus(CartStatus.IN_CART);
		}

	}

	@Override
	public List<FindAllOrderResponseDto> findAll(Long userId) {

		User user = userRepository.findByIdOrElseThrow(userId);
		List<Order> orders = new ArrayList<>();

		// usertype이 user인 경우
		if (user.getUserType().equals(UserType.USER)) {
			orders = orderRepository.findAllByUserIdOrElseThrow(userId);

		} else if (user.getUserType().equals(UserType.OWNER)) { // usertype이 owner인 경우
			Store store = storeRepository.findByUserId(userId);
			orders = orderRepository.findByStoreIdOrElseThrow(store.getId());
		}

		return orders.stream().map(order -> new FindAllOrderResponseDto(
			order.getOrderId(),
			storeService.findStoreName(order.getStoreId()),
			order.getStatus().name(),
			order.getOrderedAt()
		)).toList();
	}

	@Override
	public OwnerOrderResponseDto findOne(Long userId, Long orderId) {

		// 주문 조회
		Order order = orderRepository.findByIdOrElseThrow(orderId);
		// 본인 확인
		if (!order.getUserId().equals(userId)) {
			throw new UnauthorizedOrderAccessException();
		}

		List<MenuItemDto> menuItems = order.getCarts().stream()
			.map(MenuItemDto::toDto)
			.toList();

		return new OwnerOrderResponseDto(
			order.getOrderId(),
			menuItems,
			order.getTotalPrice(),
			order.getStatus().name(),
			order.getOrderedAt()
		);
	}
}
