package com.example.jariBean.repository.matching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MatchingRepositoryImpl implements MatchingRepositoryTemplate {
    @Autowired private MongoTemplate mongoTemplate;
}
