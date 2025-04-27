package com.example.yamyam16.menu.repository;

import com.example.yamyam16.menu.entity.Menu;
import com.example.yamyam16.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @EntityGraph(attributePaths = {"store_id"})
    Page<Menu> findAllByStoreId(Long storeId, Pageable pageable);

    Menu findByMenuNameAndStore(String menuName, Store store);
}
