package com.example.jariBean.repository.cafe;

import com.example.jariBean.entity.Cafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.geo.Metrics.KILOMETERS;


@Lazy
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
    public List<Cafe> findByIds(List<String> cafes, Pageable pageable) {
        Criteria criteria = Criteria.where("cafeId").in(cafes);
        MatchOperation matchOperation = Aggregation.match(criteria);

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.skip(pageable.getOffset()),
                Aggregation.limit(pageable.getPageSize())
        );

        return mongoTemplate.aggregate(aggregation, Cafe.class, Cafe.class).getMappedResults();
    }

    @Override
    public List<String> findByWordAndCoordinateNear(List<String> searchingWords, GeoJsonPoint point) {
        Criteria geoCriteria = new Criteria("coordinate").near(point).maxDistance(5000D);

        Criteria wordCriteria = new Criteria();
        List<Criteria> orCriterias = new ArrayList<>();

        for (String searchingWord : searchingWords) {
            Criteria regexCriteria = new Criteria("name").regex(".*" + searchingWord + ".*");
            orCriterias.add(regexCriteria);
        }
        wordCriteria.orOperator(orCriterias.toArray(new Criteria[0]));
        geoCriteria.andOperator(wordCriteria);

        Query query = new Query(geoCriteria);

        List<String> cafeIds = new ArrayList<>();
        mongoTemplate.find(query, Cafe.class).forEach(cafe -> cafeIds.add(cafe.getId()));

        return cafeIds;
    }

    @Override
    public List<Cafe> findByCoordinateNear(GeoJsonPoint point, Double distance) {
        NearQuery nearQuery = NearQuery.near(point, KILOMETERS);    // 기준 지역
        nearQuery.maxDistance(convertKiloMeterToMeter(distance));   // 반경 거리
        Criteria geoCriteria = new Criteria("coordinate").near(point).maxDistance(5000D);
        Query query = new Query(geoCriteria);
        List<Cafe> cafeList = mongoTemplate.find(query, Cafe.class);

        return cafeList;
    }

    public Double convertKiloMeterToMeter(Double KiloMeter) {
        return KiloMeter / 1000;
    }
}
