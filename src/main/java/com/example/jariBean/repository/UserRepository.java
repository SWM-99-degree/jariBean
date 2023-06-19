package com.example.jariBean.repository;

import com.example.jariBean.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {

    Optional<User> findByUserPhoneNumber(String userPhoneNumber);
    boolean existsByUserPhoneNumber(String userPhoneNumber);

}
