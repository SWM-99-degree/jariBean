package com.example.jariBean.repository.reserved;

import com.example.jariBean.entity.Reserved;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReservedRepository extends MongoRepository<Reserved, String>, ReservedRepositoryTemplate {


    List<Reserved> findByUserIdOrderByStartTimeDesc(String userId, Pageable pageable);

    List<Reserved> findByTableIdIn(List<String> tableIdList);
}
