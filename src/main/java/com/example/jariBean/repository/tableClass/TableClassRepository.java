package com.example.jariBean.repository.tableClass;

import com.example.jariBean.entity.TableClass;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TableClassRepository extends MongoRepository<TableClass, String>, TableClassRepositoryTemplate {

        Optional<TableClass> findById(String tableClassId);

        List<TableClass> findByCafeId(String cafeId);


}
