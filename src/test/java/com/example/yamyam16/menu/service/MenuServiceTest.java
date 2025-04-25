package com.example.yamyam16.menu.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.yamyam16.menu.repository.MenuRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private MenuService menuService;

	@Test
	@DisplayName("")
	void createMenu() {
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