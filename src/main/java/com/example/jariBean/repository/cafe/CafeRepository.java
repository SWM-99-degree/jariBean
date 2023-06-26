package com.example.jariBean.repository.cafe;

import com.example.jariBean.entity.cafe.Cafe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CafeRepository extends MongoRepository<Cafe, String>, CafeRepositoryTemplate {
    List<Cafe> findByCafeNameContaining(String cafeName);
}
