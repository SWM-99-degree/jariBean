package com.example.jariBean.repository.tableClass;

import com.example.jariBean.entity.TableClass;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TableClassRepository extends MongoRepository<TableClass, String>, TableClassRepositoryTemplate {
}
