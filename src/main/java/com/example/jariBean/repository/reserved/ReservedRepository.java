package com.example.jariBean.repository.reserved;

import com.example.jariBean.entity.Reserved;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReservedRepository extends MongoRepository<Reserved, String>, ReservedRepositoryTemplate {


}
