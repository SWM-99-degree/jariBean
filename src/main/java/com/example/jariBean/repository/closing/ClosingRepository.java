package com.example.jariBean.repository.closing;

import com.example.jariBean.entity.Closing;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClosingRepository extends MongoRepository<Closing, String>, ClosingRepositoryTemplate {
}
