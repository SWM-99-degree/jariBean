package com.example.jariBean.repository.table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class TableRepositoryImpl implements TableRepositoryTemplate{
    @Autowired private MongoTemplate mongoTemplate;
}
