package com.example.jariBean.repository.reserved;

import com.example.jariBean.dto.dbconnect.ReservedJoinTableDto;
import com.example.jariBean.entity.Reserved;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReservedRepositoryImpl implements ReservedRepositoryTemplate{
    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public Reserved findNearestReserved(String userId, LocalDateTime time) {
        Criteria criteria = Criteria.where("userId").is(userId).and("reservedStatus").is("VALID").and("reservedEndTime").gte(time);

        AggregationOperation match = Aggregation.match(criteria);
        AggregationOperation lookupTableClass = Aggregation.lookup("tableClass", "id", "tableClassId", "tableClass");
        AggregationOperation lookupCafe = Aggregation.lookup("cafe", "id", "cafeId", "cafe");
        AggregationOperation project = Aggregation.project("id", "userId", "cafeId", "tableId", "reservedStartTime", "reservedEndTime", "reservedStatus")
                .andExpression("cafe").arrayElementAt(0).as("cafe");
        AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "reservedStartTime");
        AggregationOperation limit = Aggregation.limit(1);

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                lookupTableClass,
                lookupCafe,
                project,
                sort,
                limit
        );

        Reserved reserved = mongoTemplate.aggregate(aggregation, Reserved.class,Reserved.class).getUniqueMappedResult();
        return reserved;
    }

    @Override
    public List<Reserved> findReservedByIdAndTableIdBetweenTime(String cafeId, String tableId, LocalDateTime time) {
        LocalDateTime startDateTime = LocalDateTime.of(time.toLocalDate(), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(time.toLocalDate(), LocalTime.MAX);

        Criteria criteria = Criteria.where("reservedStartTime").gte(startDateTime).lte(endDateTime)
                .and("tableId").is(tableId);
        Query query = new Query(criteria)
                .with(Sort.by(Sort.Direction.ASC, "reservedStartTime"));

        return mongoTemplate.find(query, Reserved.class);
    }

    @Override
    public boolean isReservedByTableIdBetweenTime(String tableId, LocalDateTime startTime, LocalDateTime endTime) {

        Criteria criteria1 = Criteria.where("reservedStartTime").gte(startTime).lt(endTime);
        Criteria criteria2 = Criteria.where("reservedEndTime").gt(startTime).lte(endTime);
        Criteria criteria3 = Criteria.where("reservedStartTime").lt(startTime).and("reservedEndTime").gt(endTime);

        return mongoTemplate.exists(new Query(Criteria.where("tableId").is(tableId)
                .orOperator(criteria1, criteria2, criteria3)), Reserved.class);
    }

    @Override
    public List<Reserved> findReservedByIdBetweenTime(String cafeId, LocalDateTime time) {

        LocalDateTime startDateTime = LocalDateTime.of(time.toLocalDate(), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(time.toLocalDate(), LocalTime.MAX);

        Criteria criteria = Criteria.where("reservedStartTime").gte(startDateTime).lte(endDateTime).and("reservedStatus").is("VALID");
        AggregationOperation sort1 = Aggregation.sort(Sort.Direction.ASC, "tableId");
        AggregationOperation sort2 = Aggregation.sort(Sort.Direction.ASC, "reservedStartTime");
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.lookup("tableClass", "id", "tableClassId", "tableClass"),
                Aggregation.lookup("cafe", "id", "cafeId", "cafe"),
                Aggregation.project("id", "userId", "cafeId", "tableId", "reservedStartTime", "reservedEndTime")
                        .andExpression("tableClass").arrayElementAt(0).as("tableClass"),
                sort1,
                sort2
        );

        return mongoTemplate.aggregate(aggregation, Reserved.class, Reserved.class).getMappedResults();
    }



}
