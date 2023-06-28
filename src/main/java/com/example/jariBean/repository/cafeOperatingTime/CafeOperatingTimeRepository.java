package com.example.jariBean.repository.cafeOperatingTime;

import com.example.jariBean.entity.CafeOperatingTime;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CafeOperatingTimeRepository extends MongoRepository<CafeOperatingTime, String>, CafeOperatingTimeTemplate {

}
