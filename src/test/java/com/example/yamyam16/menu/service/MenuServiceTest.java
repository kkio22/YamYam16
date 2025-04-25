package com.example.yamyam16.menu.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.yamyam16.menu.MenuStatus;
import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.menu.repository.MenuRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private MenuService menuService;

	@Test
	@DisplayName("")
	void getStoreByStoreId_whenStoreExists() {
		//given
		Menu menu1 = new Menu("불고기버거", 5000, MenuStatus.AVAILABLE);
		Menu menu2 = new Menu("치즈버거", 4500, MenuStatus.HOLDOUT);
		Menu menu3 = new Menu("새우버거", 4800, MenuStatus.HOLDOUT);
		Menu menu4 = new Menu("더블불고기버거", 6000, MenuStatus.AVAILABLE);

	}

	@Test
	void findMenuByPage() {
	}

	@Test
	void updateMenu() {
	}

	@Test
	void deleteMenu() {
	}
}