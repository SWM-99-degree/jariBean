package com.example.jariBean.repository.closing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class ClosingRepositoryImpl implements ClosingRepositoryTemplate{

    @Autowired private MongoTemplate mongoTemplate;
}
