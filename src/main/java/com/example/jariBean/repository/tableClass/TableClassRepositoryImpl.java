package com.example.jariBean.repository.tableClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class TableClassRepositoryImpl implements TableClassRepositoryTemplate{

    @Autowired private MongoTemplate mongoTemplate;
}
