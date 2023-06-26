package com.example.jariBean.repository;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CafeRepository extends MongoRepository<Cafe, Long> {

    Optional<Cafe> findByCafePhoneNumber(String cafePhoneNumber);
    boolean existsByCafePhoneNumber(String cafePhoneNumber);
}
