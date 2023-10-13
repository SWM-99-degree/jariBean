package com.example.jariBean.repository.matching;

import com.example.jariBean.entity.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

public interface MatchingRepository extends MongoRepository<Matching, String>, MatchingRepositoryTemplate {

    Page<Matching> findByUserIdOrderByMatchingTimeDesc(String userId, Pageable pageable);

}
