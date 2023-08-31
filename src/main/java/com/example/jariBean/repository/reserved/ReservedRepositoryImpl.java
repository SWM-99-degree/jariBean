package com.example.jariBean.repository.reserved;

import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.TableClass;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class ReservedRepositoryImpl implements ReservedRepositoryTemplate{
    @Autowired private MongoTemplate mongoTemplate;

    public static class Tuple<cafeId, tableId>{
        private final String cafeId;
        private final String tableId;

        public Tuple(String cafeId, String tableId) {
            this.cafeId = cafeId;
            this.tableId = tableId;
        }
    }


    @Override
    public List<String> findCafeByReserved(List<String> cafes, LocalDateTime startTime, LocalDateTime endTime, Integer seating, List<TableClass.TableOption> tableOptionList) {

        Map<String, Set> filterCafes = new HashMap<>();
        Criteria mainCriteria = new Criteria();

        if (cafes != null) {
            mainCriteria.and("cafeId").in(cafes);
        }
        if (!tableOptionList.isEmpty()) {
            mainCriteria.and("table.tableOptionList").all(tableOptionList);
        }
        if (seating != null) {
            mainCriteria.and("table.seating").gte(seating);
        }

        Query queryByWordsandOptions = new Query(mainCriteria);
        mongoTemplate.find(queryByWordsandOptions, Reserved.class).forEach(reserved ->
                {
                    if (filterCafes.containsKey(reserved.getCafe().getId())) {
                        filterCafes.get(reserved.getCafe().getId()).add(reserved.getTable().getId());
                    } else {
                        Set<String> tableSet = new HashSet<>();
                        tableSet.add(reserved.getTable().getId());
                        filterCafes.put(reserved.getCafe().getId(), tableSet);
                    }
                });

        Criteria reservedCriteria = new Criteria();

        if (startTime != null){
            // 겹치는 카페 예약
            Criteria case1Criteria = Criteria.where("startTime").gte(startTime).lt(endTime);
            Criteria case2Criteria = Criteria.where("endTime").gt(startTime).lte(endTime);
            Criteria case3Criteria = Criteria.where("startTime").lt(startTime).and("endTime").gt(endTime);

            reservedCriteria.orOperator(
                    case1Criteria, case2Criteria, case3Criteria
            );

            Query queryByTime = new Query(reservedCriteria);
            mongoTemplate.find(queryByTime, Reserved.class).forEach(reserved -> {
                if (filterCafes.containsKey(reserved.getCafe().getId())) {filterCafes.get(reserved.getCafe().getId()).remove(reserved.getTable().getId());}
            });
        }

        List<String> canReserveCafes = new ArrayList<>();
        for (Map.Entry<String, Set> cafe : filterCafes.entrySet()) {
            if (!cafe.getValue().isEmpty()) {canReserveCafes.add(cafe.getKey());}}

        return canReserveCafes;
    }

    @Override
    public Reserved findNearestReserved(String userId, LocalDateTime time) {
        Criteria criteria = Criteria.where("userId").is(userId).and("status").is("VALID").and("endTime").gte(time);

        AggregationOperation match = Aggregation.match(criteria);
        AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "startTime");
        AggregationOperation limit = Aggregation.limit(1L);

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                sort,
                limit
        );

        Reserved reserved = mongoTemplate.aggregate(aggregation, Reserved.class,Reserved.class).getUniqueMappedResult();
        return reserved;
    }

    @Override
    public boolean isReservedByTableIdBetweenTime(String tableId, LocalDateTime startTime, LocalDateTime endTime) {

        Criteria criteria1 = Criteria.where("startTime").gte(startTime).lt(endTime);
        Criteria criteria2 = Criteria.where("endTime").gt(startTime).lte(endTime);
        Criteria criteria3 = Criteria.where("startTime").lt(startTime).and("endTime").gt(endTime);

        return mongoTemplate.exists(new Query(Criteria.where("tableId").is(tableId)
                .orOperator(criteria1, criteria2, criteria3)), Reserved.class);
    }

    @Override
    public List<Reserved> findReservedByIdBetweenTime(String cafeId, LocalDateTime startTime, LocalDateTime endTime) {
        // 절대 예약되어 있으면 안되는 부분
        LocalDateTime tableStartTime = startTime.minusHours(1);
        LocalDateTime tableEndTime = endTime.plusHours(1);

        startTime = startTime.plusMinutes(30);
        endTime = endTime.minusMinutes(30);
        Criteria tableLimitCriteria = Criteria.where("startTime").lte(tableEndTime).and("endTime").gte(tableStartTime);
        Criteria criteria1 = Criteria.where("startTime").gte(startTime).lt(endTime);
        Criteria criteria2 = Criteria.where("endTime").gt(startTime).lte(endTime);
        Criteria criteria3 = Criteria.where("startTime").lt(startTime).and("endTime").gt(endTime);

        AggregationOperation sort1 = Aggregation.sort(Sort.Direction.ASC, "tableId");
        AggregationOperation sort2 = Aggregation.sort(Sort.Direction.ASC, "startTime");
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(tableLimitCriteria),
                Aggregation.match(criteria1),
                Aggregation.match(criteria2),
                Aggregation.match(criteria3),
                Aggregation.lookup("tableClass", "id", "tableClassId", "tableClass"),
                Aggregation.lookup("cafe", "id", "cafeId", "cafe"),
                Aggregation.project("id", "userId", "cafeId", "tableId", "startTime", "endTime")
                        .andExpression("tableClass").arrayElementAt(0).as("tableClass"),
                sort1,
                sort2
        );

        return mongoTemplate.aggregate(aggregation, Reserved.class, Reserved.class).getMappedResults();
    }

    @Override
    public List<Reserved> findReservedByConditions(String cafeId, LocalDateTime startTime, Integer seating, List<TableClass.TableOption> tableOptionList) {


        Criteria mainCriteria = new Criteria("cafe._id").is(new ObjectId(cafeId));

        mainCriteria.and("startTime").gte(startTime.with(LocalTime.MIN)).lte(startTime.with(LocalTime.MAX));

        if (!tableOptionList.isEmpty()) {
            mainCriteria.and("table.tableOptionList").all(tableOptionList);
        }
        if (seating != null) {
            mainCriteria.and("table.seating").gte(seating);
        }

        MatchOperation matchTable = Aggregation.match(mainCriteria);

        Aggregation aggregation = Aggregation.newAggregation(matchTable);

        return mongoTemplate.aggregate(aggregation, "reserved", Reserved.class).getMappedResults();
    }


    @Override
    public List<Reserved> findTodayReservedById(String cafeId, LocalDateTime time) {

        Criteria criteria = new Criteria("startTime").gte(time.with(LocalTime.MIN)).lte(time.with(LocalTime.MAX));

        criteria.and("cafe._id").is(new ObjectId(cafeId));

        MatchOperation matchToday = Aggregation.match(criteria);

        Aggregation aggregation = Aggregation.newAggregation(matchToday);

        return mongoTemplate.aggregate(aggregation, "reserved", Reserved.class).getMappedResults();
    }
}
