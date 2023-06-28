package com.example.jariBean.repository.cafeOperatingTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class CafeOperatingTimeRepositoryImpl implements CafeOperatingTimeTemplate{

    @Autowired private MongoTemplate mongoTemplate;

}
