package com.example.jariBean.repository.cafe;

import com.example.jariBean.entity.Cafe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

public interface CafeRepositoryTemplate {

    List<String> findByWordAndCoordinateNear(List<String> searchingWords,GeoJsonPoint point);


    List<Cafe> findByCoordinateNear(GeoJsonPoint point, Double distance);

    Page<Cafe> findByIds(List<String> cafes, Pageable pageable);

    Cafe findByIdwithOperatingTime(String cafeId);

}
