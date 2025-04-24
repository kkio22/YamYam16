package com.example.yamyam16.store.repository;

import com.example.yamyam16.auth.entity.User;
import com.example.yamyam16.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<User> findById(Store store);
}
