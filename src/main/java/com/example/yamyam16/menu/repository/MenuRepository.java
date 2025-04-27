package com.example.yamyam16.menu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.yamyam16.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	@EntityGraph(attributePaths = {"store"})
	Page<Menu> findAllByStoreId(Long storeId, Pageable pageable);

	Menu findByMenuNameAndStore_Id(String menuName, Long storeId);
}
