package com.example.yamyam16.menu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	Page<Menu> findAllByStoreId(Long storeId, Pageable pageable);
}
