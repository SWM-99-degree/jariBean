package com.example.jariBean.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class UserRepositoryImpl implements UserRepositoryTemplate{

    @Autowired private MongoTemplate mongoTemplate;

}
