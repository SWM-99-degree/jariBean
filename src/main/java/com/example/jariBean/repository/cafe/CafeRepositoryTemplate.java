package com.example.jariBean.repository.cafe;

import com.example.jariBean.entity.Cafe;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepositoryTemplate {
    List<Cafe> findByCoordinateNear(GeoJsonPoint point, Double distance);
}
