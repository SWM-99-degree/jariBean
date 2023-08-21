package com.example.jariBean.repository.user;

import com.example.jariBean.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryTemplate {

    Optional<User> findBySocialId(String userPhoneNumber);
    boolean existsBySocialId(String socialId);


}
