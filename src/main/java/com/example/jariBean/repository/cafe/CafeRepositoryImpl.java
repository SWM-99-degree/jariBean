package com.example.jariBean.repository.cafe;

import com.example.jariBean.dto.dbconnect.CafeJoinOperatingTimeDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.CafeOperatingTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.geo.Metrics.*;


public class CafeRepositoryImpl implements CafeRepositoryTemplate{

    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public CafeJoinOperatingTimeDto findByIdwithOperatingTime(String cafeId) {
        Criteria criteria = Criteria.where("cafeId").is(cafeId);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.lookup("cafeoperatingtime", "cafeid", "cafeid", "cafe"),
                Aggregation.project("cafeId", "cafeName", "cafeImg")
                        .and("cafeoperatingtime.cafestarttime").as("cafeStartTime")
                        .and("cafeoperatingtime.cafeendtime").as("cafeEndTime")
        );

        CafeJoinOperatingTimeDto cafeJoinOperatingTimeDto = mongoTemplate.aggregate(aggregation, Cafe.class,CafeJoinOperatingTimeDto.class).getUniqueMappedResult();
        return cafeJoinOperatingTimeDto;
    }

    @Override
    public List<Cafe> findByCoordinateNear(GeoJsonPoint point, Double distance) {
        NearQuery nearQuery = NearQuery.near(point, KILOMETERS);    // 기준 지역
        nearQuery.maxDistance(convertKiloMeterToMeter(distance));   // 반경 거리

        List<Cafe> cafeList = new ArrayList<>();
        mongoTemplate.geoNear(nearQuery, Cafe.class, "cafe", Cafe.class)
                .forEach(cafeGeoResult -> {
                    cafeList.add(cafeGeoResult.getContent());
        });

        return cafeList;
    }

    public Double convertKiloMeterToMeter(Double KiloMeter) {
        return KiloMeter / 1000;
    }
}
