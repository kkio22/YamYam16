package com.example.yamyam16.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.auth.entity.UserType;
import com.example.yamyam16.auth.repository.UserRepository;
import com.example.yamyam16.cart.dto.request.DeleteCartRequestDto;
import com.example.yamyam16.cart.entity.Cart;
import com.example.yamyam16.cart.enums.CartStatus;
import com.example.yamyam16.cart.repository.CartRepository;
import com.example.yamyam16.menu.MenuStatus;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.order.dto.request.ChangeOrderStatusRequestDto;
import com.example.yamyam16.order.dto.response.ChangeOrderStatusResponseDto;
import com.example.yamyam16.order.dto.response.FindAllOrderResponseDto;
import com.example.yamyam16.order.dto.response.OwnerOrderResponseDto;
import com.example.yamyam16.order.dto.response.UserOrderResponseDto;
import com.example.yamyam16.order.entity.Order;
import com.example.yamyam16.order.enums.OrderStatus;
import com.example.yamyam16.order.repository.OrderRepository;
import com.example.yamyam16.store.entity.CategoryType;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;
import com.example.yamyam16.store.service.StoreService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

	@Mock
	private CartRepository cartRepository;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private StoreService storeService;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private OrderServiceImpl orderService;

	@Test
	@DisplayName("주문 저장 성공")
	void saveOrder() {
		// given
		Long userId = 1L;
		Store store = Store.builder()
			.name("Test Store")
			.openTime(9L)
			.closeTime(22L)
			.minOrderPrice(10000L)
			.category(CategoryType.Westerncuisine)
			.build();

		Cart cart = new Cart(userId, null, 2L);
		cart.setStatus(CartStatus.IN_CART);
		ReflectionTestUtils.setField(cart,"menu",mockMenuWithStore(store, 5000L));

		when(cartRepository.findByUserIdAndStatus(userId, CartStatus.IN_CART)).thenReturn(List.of(cart));

		// when
		UserOrderResponseDto response = orderService.save(userId);

		// then
		assertThat(response.getTotalPrice()).isEqualTo(10000L);
		assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.ORDERED.name());
	}

	@Test
	@DisplayName("주문 수락 성공")
	void acceptOrder() {
		// given
		Long userId = 1L;
		Long orderId = 1L;

		Store store = Store.builder()
			.name("Test Store")
			.openTime(9L)
			.closeTime(22L)
			.minOrderPrice(10000L)
			.category(CategoryType.Westerncuisine)
			.build();

		User user = new User();
		ReflectionTestUtils.setField(user,"id",userId);
		ReflectionTestUtils.setField(store,"user",user);

		Order order = new Order(userId, store.getId(), 10000L, OrderStatus.ORDERED, LocalDateTime.now());

		when(orderRepository.findByIdOrElseThrow(orderId)).thenReturn(order);
		when(storeRepository.findByIdOrElseThrow(userId)).thenReturn(store);
		when(cartRepository.findByOrders_OrderId(orderId)).thenReturn(Collections.emptyList());

		// when
		OwnerOrderResponseDto response = orderService.accept(userId, orderId);

		// then
		assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.PREPARING.name());
	}

	@Test
	@DisplayName("주문 상태 변경 성공")
	void changeOrderStatus() {
		// given
		Long userId = 1L;
		Long orderId = 1L;

		Order order = new Order(userId, 1L, 10000L, OrderStatus.ORDERED, LocalDateTime.now());
		when(orderRepository.findByIdOrElseThrow(orderId)).thenReturn(order);

		ChangeOrderStatusRequestDto statusRequestDto = new ChangeOrderStatusRequestDto(OrderStatus.DELIVERING);

		// when
		ChangeOrderStatusResponseDto response = orderService.changeStatus(userId, orderId, statusRequestDto);

		// then
		assertThat(response.getStatus()).isEqualTo(OrderStatus.DELIVERING.name());
	}

	@Test
	@DisplayName("주문 취소 성공")
	void cancelOrder() {
		// given
		Long orderId = 1L;
		Long loginUserId = 1L;

		Order order = new Order(loginUserId, 1L, 10000L, OrderStatus.ORDERED, LocalDateTime.now());
		when(orderRepository.findByIdOrElseThrow(orderId)).thenReturn(order);

		// when
		orderService.cancelOrder(orderId, loginUserId);

		// then
		assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
	}

	@Test
	@DisplayName("주문 전체 조회 성공 (유저)")
	void findAllOrders_User() {
		// given
		Long userId = 1L;
		User user = new User();
		ReflectionTestUtils.setField(user,"id",userId);
		ReflectionTestUtils.setField(user,"userType",UserType.USER);

		Order order = new Order(userId, 1L, 10000L, OrderStatus.ORDERED, LocalDateTime.now());

		when(userRepository.findByIdOrElseThrow(userId)).thenReturn(user);
		when(orderRepository.findAllByUserIdOrElseThrow(userId)).thenReturn(List.of(order));
		when(storeService.findStoreName(order.getStoreId())).thenReturn("Test Store");

		// when
		List<FindAllOrderResponseDto> responses = orderService.findAll(userId);

		// then
		assertThat(responses).hasSize(1);
	}

	@Test
	@DisplayName("주문 상세 조회 성공")
	void findOneOrder() {
		// given
		Long userId = 1L;
		Long orderId = 1L;

		Order order = new Order(userId, 1L, 10000L, OrderStatus.ORDERED, LocalDateTime.now());
		ReflectionTestUtils.setField(order, "orderId", orderId);
		when(orderRepository.findByIdOrElseThrow(orderId)).thenReturn(order);

		// when
		OwnerOrderResponseDto response = orderService.findOne(userId, orderId);

		// then
		assertThat(response.getOrderId()).isEqualTo(orderId);
	}

	// ===== Helper Method =====

	private Menu mockMenuWithStore(Store store, Long price) {
		Menu menu = new Menu("Burger", price, MenuStatus.AVAILABLE);
		menu.setStore(store);
		return menu;
	}
}