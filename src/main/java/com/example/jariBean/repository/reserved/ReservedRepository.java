package com.example.jariBean.repository.reserved;

import com.example.jariBean.entity.Reserved;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedRepository extends MongoRepository<Reserved, String>, ReservedRepositoryTemplate {

    List<Reserved> findByUserId(String userId);


    Page<Reserved> findByUserIdOrderByStartTimeAsc(String userId, Pageable pageable);

    List<Reserved> findByTableIdIn(List<String> tableIdList);


}
