package com.example.jariBean.repository.cafemanager;

import com.example.jariBean.entity.CafeManager;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CafeManagerRepository extends MongoRepository<CafeManager, String> {
    Optional<CafeManager> findByEmail(String email);
    boolean existsByEmail(String email);
}
