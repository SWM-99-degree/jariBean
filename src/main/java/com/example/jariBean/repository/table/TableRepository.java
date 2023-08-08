package com.example.jariBean.repository.table;

import com.example.jariBean.entity.Table;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TableRepository extends MongoRepository<Table, String>, TableRepositoryTemplate {

    List<Table> findByCafeId(String cafeId);

    List<Table> findByTableClassId(String tableClassId);

}
