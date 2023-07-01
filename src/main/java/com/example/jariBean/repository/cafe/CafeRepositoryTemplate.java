package com.example.jariBean.repository.cafe;

import com.example.jariBean.dto.dbconnect.CafeJoinOperatingTimeDto;
import com.example.jariBean.entity.Cafe;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

public interface CafeRepositoryTemplate {
    List<Cafe> findByCoordinateNear(GeoJsonPoint point, Double distance);


    CafeJoinOperatingTimeDto findByIdwithOperatingTime(String cafeId);
}
