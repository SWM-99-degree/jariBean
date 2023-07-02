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
    public ReservedJoinTableDto findNearestReserved(String userId, LocalDateTime time) {
        Criteria criteria = Criteria.where("userId").is(userId).and("reservedStatus").is("VALID").and("reservedStartTime").gte(time);

        AggregationOperation match = Aggregation.match(criteria);
        AggregationOperation lookupTable = Aggregation.lookup("table", "tableId", "tableId", "table");
        AggregationOperation lookupCafe = Aggregation.lookup("cafe", "cafeId", "cafeId", "cafe");
        AggregationOperation lookupTableClass = Aggregation.lookup("tableClass", "tableClassId", "table.tableClassId", "tableClass");
        AggregationOperation project = Aggregation.project("id", "userId", "cafeId", "tableId", "reservedStartTime", "reservedEndTime", "reservedStatus")
                .andExpression("cafe.cafeName").as("cafeName")
                .andExpression("cafe.cafeImg").as("cafeImg")
                .andExpression("tableClass.tableOptions").arrayElementAt(0).as("tableOptions");
        AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "reservedStartTime");
        AggregationOperation limit = Aggregation.limit(1);

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                lookupTable,
                lookupCafe,
                lookupTableClass,
                project,
                sort,
                limit
        );


        ReservedJoinTableDto reservedJoinTableDto = mongoTemplate.aggregate(aggregation, Reserved.class,ReservedJoinTableDto.class).getUniqueMappedResult();
        return reservedJoinTableDto;
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
    public List<ReservedJoinTableDto> findReservedByIdBetweenTime(String cafeId, LocalDateTime time) {

        LocalDateTime startDateTime = LocalDateTime.of(time.toLocalDate(), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(time.toLocalDate(), LocalTime.MAX);

        Criteria criteria = Criteria.where("reservedStartTime").gte(startDateTime).lte(endDateTime).and("reservedStatus").is("VALID");
        AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "reservedStartTime");
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.lookup("tableClass", "_id", "tableId", "tableClass"),
                Aggregation.lookup("cafe", "_id", "cafeId", "cafe"),
                Aggregation.unwind("tableClass", true),
                Aggregation.unwind("cafe", true),
                Aggregation.project("id", "userId", "cafeId", "tableId", "reservedStartTime", "reservedEndTime")
                        .andExpression("tableClass.tableClassOptions").arrayElementAt(0).as("tableClassOptions"),
                sort
        );

        return mongoTemplate.aggregate(aggregation, Reserved.class, ReservedJoinTableDto.class).getMappedResults();
    }


}
