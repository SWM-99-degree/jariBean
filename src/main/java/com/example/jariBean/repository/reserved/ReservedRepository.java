package com.example.jariBean.repository.reserved;

import com.example.jariBean.entity.Reserved;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservedRepository extends MongoRepository<Reserved, String>, ReservedRepositoryTemplate {
}
