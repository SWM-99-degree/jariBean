package com.example.jariBean.repository.cafe;

import com.example.jariBean.entity.Cafe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CafeRepository extends MongoRepository<Cafe, String>, CafeRepositoryTemplate {
    Optional<Cafe> findByCafePhoneNumber(String cafePhoneNumber);
    boolean existsByCafePhoneNumber(String cafePhoneNumber);
}
