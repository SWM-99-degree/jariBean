package com.example.jariBean.repository.cafeOperatingTime;

import com.example.jariBean.entity.CafeOperatingTime;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CafeOperatingTimeRepository extends MongoRepository<CafeOperatingTime, String>, CafeOperatingTimeTemplate {

    Optional<CafeOperatingTime> findById(String cafeId);
}
