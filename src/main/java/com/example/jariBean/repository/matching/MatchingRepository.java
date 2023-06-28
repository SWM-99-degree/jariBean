package com.example.jariBean.repository.matching;

import com.example.jariBean.entity.Matching;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchingRepository extends MongoRepository<Matching, String>, MatchingRepositoryTemplate {
}
