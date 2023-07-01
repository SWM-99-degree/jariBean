package com.example.jariBean.repository.table;

import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TableRepository extends MongoRepository<Table, String>, TableRepositoryTemplate {

}
