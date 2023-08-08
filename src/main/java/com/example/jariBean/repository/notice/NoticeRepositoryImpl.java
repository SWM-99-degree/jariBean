package com.example.jariBean.repository.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;


public class NoticeRepositoryImpl implements NoticeRepositoryTemplate {

    @Autowired private MongoTemplate mongoTemplate;

}
