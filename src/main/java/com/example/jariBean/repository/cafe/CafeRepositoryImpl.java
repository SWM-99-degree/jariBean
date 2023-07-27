package com.example.jariBean.repository.cafe;

import com.example.jariBean.dto.dbconnect.CafeJoinOperatingTimeDto;
import com.example.jariBean.entity.Cafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.geo.Metrics.KILOMETERS;


public class CafeRepositoryImpl implements CafeRepositoryTemplate{

    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public Cafe findByIdwithOperatingTime(String cafeId) {
        Criteria criteria = Criteria.where("id").is(cafeId);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.lookup("cafeOperatingTime", "cafeId", "id", "cafeOperatingTime"),
                Aggregation.project("cafeId", "cafeName", "cafeImg")
                        .andExpression("cafeOperatingTime").as("cafeOperatingTimeList")
        );

        Cafe cafe = mongoTemplate.aggregate(aggregation, Cafe.class, Cafe.class).getUniqueMappedResult();
        return cafe;
    }

    @Override
    public List<Cafe> findById(List<String> cafes) {
        Criteria criteria = Criteria.where("cafeId").in(cafes);
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, Cafe.class);
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
