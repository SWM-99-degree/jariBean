package com.example.jariBean.repository.table;

import com.example.jariBean.entity.Table;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TableRepository extends MongoRepository<Table, String>, TableRepositoryTemplate {
}
