package com.example.jariBean.repository.reserved;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class ReservedRepositoryImpl implements ReservedRepositoryTemplate{
    @Autowired private MongoTemplate mongoTemplate;
}
