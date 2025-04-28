package com.example.yamyam16.menu.service;

import static com.example.yamyam16.menu.MenuStatus.*;
import static com.example.yamyam16.store.entity.CategoryType.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.yamyam16.exception.CustomException;
import com.example.yamyam16.menu.dto.MenuCreateRequestDto;
import com.example.yamyam16.menu.dto.MenuCreateResponseDto;
import com.example.yamyam16.menu.dto.MenuListResponseDto;
import com.example.yamyam16.menu.dto.MenuUpdateRequestDto;
import com.example.yamyam16.menu.dto.MenuUpdateResponseDto;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.menu.repository.MenuRepository;
import com.example.yamyam16.store.entity.Store;
import com.example.yamyam16.store.repository.StoreRepository;

//test는 단위 테스트로 내가 만든 매서드의 핵심 기능만 테스트 하기에 생성이면 생성이 되는지만 보고, store가 있는지는 확인할 필요 없음
//store가 있는지는 예외로 따로 빼서 해야 함
//test는 한가지 기능만 테스트 하는 것임
//엔티티 객체여서 mock을 할 필요가 없다.
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private MenuService menuService;
	private Store store; //순수 자바 객체여서 의존성 주입 필요없음 전역변수

	@BeforeEach
		//(공통으로 필요하면 사용)
	void setUp() {
		Long storeId = 1L;

		store = Store.builder()
			.name("다 있는 음식점")
			.openTime(8L)
			.closeTime(10L)
			.minOrderPrice(15000L)
			.category(Koreancusine)
			.build();

		ReflectionTestUtils.setField(store, "id", storeId); //store 객체의 id필드가 private이면 리플렉션을 통해 값을 설정할 수 있다.

	}
	// 실제 로직에 없으면 작성하면 안 됨

	@Test
	@DisplayName("메뉴 생성이되었는지 확인")
	void createMenuSuccess() {
		//store 확인 -> 연관관계가 맺어져 있어서 조건으로 넣어줘야 함, 유효성 검사에서 필요한가를 생각해서 필요하면 넣어야 함
		Long storeId = 1L; //지역변수 & 전역변수

		given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

		//menu 생성
		Menu menu = new Menu(
			"치킨",
			13000L,
			AVAILABLE //import때문에 그냥 이렇게 작성해도 됨
		);

		when(menuRepository.save(any(Menu.class))).thenReturn(menu); //save한 객체는 optional로 감쌀 필요가 없다.

		MenuCreateRequestDto menuCreateRequestDto = new MenuCreateRequestDto("치킨", 13000L, AVAILABLE);

		MenuCreateResponseDto menuCreateResponseDto = menuService.createMenu(storeId, menuCreateRequestDto);

		assertThat(menuCreateResponseDto.getMenuName()).isEqualTo("치킨");
		assertThat(menuCreateResponseDto.getMenuPrice()).isEqualTo(13000L);
		assertThat(menuCreateResponseDto.getMenuStatus()).isEqualTo("판매중");

	}

	@Test
	@DisplayName("메뉴가 조회되는지 확인")
	void findMenuByPageSuccess() {
		Long storeId = 1L;
		Long page = 1L;
		Long size = 10L;

		given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

		List<Menu> dummyMenus = Arrays.asList(
			new Menu("치킨", 13000L, AVAILABLE),
			new Menu("불고기버거", 5000L, AVAILABLE),
			new Menu("피자", 15000L, SOLDOUT),
			new Menu("떡볶이", 8000L, AVAILABLE),
			new Menu("짜장면", 6000L, AVAILABLE),
			new Menu("김밥", 3000L, AVAILABLE),
			new Menu("볶음밥", 7000L, AVAILABLE),
			new Menu("돈까스", 12000L, SOLDOUT),
			new Menu("라면", 4000L, AVAILABLE),
			new Menu("갈비", 20000L, AVAILABLE)
		);

		dummyMenus.forEach(menu -> menu.setStore(store));

		// menuPrice에 따라 오름차순 정렬
		List<Menu> sortedMenus = dummyMenus.stream()
			.sorted(Comparator.comparingLong(Menu::getMenuPrice))
			.collect(Collectors.toList()); //

		// Pageable 설정
		Pageable pageable = PageRequest.of(page.intValue() - 1, size.intValue(), Sort.by("menuPrice").ascending());

		// 정렬된 메뉴 리스트를 이용해 PageImpl 생성
		Page<Menu> menuPage = new PageImpl<>(sortedMenus, pageable, sortedMenus.size());

		when(menuRepository.findAllByStoreId(eq(storeId), eq(pageable))).thenReturn(menuPage);

		// when
		List<MenuListResponseDto> response = menuService.findMenuByPage(storeId, page, size);

		// then
		// 첫 번째 메뉴는 가격이 가장 낮은 "김밥"이어야 함
		assertThat(response.get(0).getMenuName()).isEqualTo("김밥");
		assertThat(response.get(9).getMenuName()).isEqualTo("갈비");
		assertThat(response.get(9).getMenuPrice()).isEqualTo(20000L);
	}

	@Test
	@DisplayName("메뉴가 수정되는지 확인")
	void updateMenuSuccess() {
		Long storeId = 1L;
		Long menuId = 1L;

		given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
		// 메뉴 수정
		Menu menu = Menu.builder()
			.menuName("치킨")
			.menuPrice(13000L)
			.menuStatus(AVAILABLE)
			.build();

		when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

		MenuUpdateRequestDto menuUpdateRequestDto = new MenuUpdateRequestDto("마라탕", 9000L, SOLDOUT);

		MenuUpdateResponseDto menuUpdateResponseDto = menuService.updateMenu(storeId, menuId, menuUpdateRequestDto);

		assertEquals("마라탕", menuUpdateResponseDto.getMenuName());
		assertEquals(9000L, menuUpdateResponseDto.getMenuPrice());
		assertEquals("품절", menuUpdateResponseDto.getMenuStatus());

	}

	@Test
	@DisplayName("메뉴가 삭제되는지 확인")
	void deleteMenuSuccess() {
		Long storeId = 1L;
		Long menuId = 1L;

		given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

		//메뉴 삭제
		Menu menu = Menu.builder()
			.menuName("치킨")
			.menuPrice(13000L)
			.menuStatus(AVAILABLE)
			.build();

		//given은 테스트 시작 전에 있는 조건, 세팅하는 매서드, 진짜 있는 매서드를 호출할 때는 사용하지 않고 mock 객체를 호출할 때 사용
		given(menuRepository.findById(menuId)).willReturn(Optional.of(menu)); //찾을 때는 없을 수도 있으니 optional로 감싼다.

		//when은 테스트에서 어떤 행위를 하는 것, 진짜 있는 매서드를 호출할 때는 사용하지 않고 mock 객체를 호출할 때 사용
		menuService.deleteMenu(storeId, menuId);

		assertThat(menu.getMenuStatus()).isEqualTo(DELETED); //소프트 삭제여서 품절로 바뀌었는지 확인

		verify(menuRepository, times(0)).deleteById(
			menuId); //verify는 검증하는 로직이다. menuRepository에서 매서드가 0번 호출되었는지 검증한다. 그리고 deletById(menuId)로 menuRepositoy에서 삭제한다.

		assertThat(menuRepository.findById(menuId)).isPresent(); //존재하는지 확인
	}

	@Test
	@DisplayName("가게가 없으면 메뉴 생성할 때 예외 발생")
	void creatMenuWhenStoreIdNotFound() {
		Long storeId = null;

		MenuCreateRequestDto menuCreateRequestDto = new MenuCreateRequestDto("치킨", 13000L, AVAILABLE);

		CustomException customException = assertThrows(CustomException.class, () -> { //예외 타입 클래스를 나타냄
			menuService.createMenu(storeId, menuCreateRequestDto); //람다식 : (매개변수)  -> {실행할 코드}, 매개변수는 없으면 비워둠
			// 이거로 보고 싶은게 service가 실행되면 예외가 발생하는지임 -> 그래서 람다식으로 서비스 나타내고, 실행될 때 예외가 발생함 그래서 자료형이 예외 클래스임
		});

		assertEquals(404, customException.getErrorCode().getStatus());
		assertEquals("Not Found", customException.getErrorCode().getError());
		assertEquals("S001", customException.getErrorCode().getCode());
		assertEquals("조회된 가게가 없습니다.", customException.getErrorCode().getMessage());

	}

	@Test
	@DisplayName("수정할 메뉴의 메뉴 아이디가 없을 때 예외 발생")
	void updateMenuWhenMenuIdNotFound() {
		Long storeId = 1L;
		Long menuId = 1L;

		given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

		//Optional.empty() 빈 객체를 만드는 것 / isEmpty = 비어있는지 확인하는 것
		when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

		MenuUpdateRequestDto menuUpdateRequestDto = new MenuUpdateRequestDto("마라탕", 9000L, SOLDOUT);

		CustomException customException = assertThrows(CustomException.class,
			() -> menuService.updateMenu(storeId, menuId, menuUpdateRequestDto));

		assertEquals(404, customException.getErrorCode().getStatus());
		assertEquals("Not Found", customException.getErrorCode().getError());
		assertEquals("M002", customException.getErrorCode().getCode());
		assertEquals("조회된 메뉴가 없습니다.", customException.getErrorCode().getMessage());

	}

	@Test
	@DisplayName("삭제할 메뉴의 메뉴 아이디가 없을 때 예외 발생")
	void deleteMenuWhenMenuIdNotFound() {
		Long storeId = 1L;
		Long menuId = 1L;

		given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

		//Optional.empty() 빈 객체를 만드는 것 / isEmpty = 비어있는지 확인하는 것
		when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> menuService.deleteMenu(storeId, menuId));

		assertEquals(404, customException.getErrorCode().getStatus());
		assertEquals("Not Found", customException.getErrorCode().getError());
		assertEquals("M002", customException.getErrorCode().getCode());
		assertEquals("조회된 메뉴가 없습니다.", customException.getErrorCode().getMessage());

	}

}

