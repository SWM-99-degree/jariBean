package com.example.jariBean.repository.reserved;

import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.TableClass;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Reserved> findReservedByConditions(String cafeId, LocalDateTime startTime, LocalDateTime endTime, Integer seating, List<TableClass.TableOption> tableOptionList) {

        Criteria mainCriteria = new Criteria("cafe._id").is(cafeId);

        // 겹치는 카페 예약
        if (startTime != null) {
            LocalDateTime newStartTime = startTime.plusMinutes(30);
            LocalDateTime newEndTime = endTime.minusMinutes(30);
            Criteria reservedCriteria = new Criteria();
            Criteria case1Criteria = Criteria.where("reservedStartTime").gte(newStartTime).lt(newEndTime);
            Criteria case2Criteria = Criteria.where("reservedEndTime").gt(newStartTime).lte(newEndTime);
            Criteria case3Criteria = Criteria.where("reservedStartTime").lt(newStartTime).and("reservedEndTime").gt(newEndTime);

            reservedCriteria.orOperator(
                    case1Criteria, case2Criteria, case3Criteria
            );
            Set<String> tableIdList = new HashSet<>();
            Query query = new Query(reservedCriteria);
            mongoTemplate.find(query, Reserved.class).forEach(reserved -> tableIdList.add(reserved.getTable().getId()));
            mainCriteria.and("table.tableId").in(tableIdList.stream().toList());
            mainCriteria.and("reservedStartTime").gte(startTime.with(LocalTime.MIN)).lt(startTime.with(LocalTime.MAX));
        } else {
            LocalDateTime today = LocalDateTime.now();
            mainCriteria.and("reservedStartTime").gte(today.with(LocalTime.MIN)).lt(today.with(LocalTime.MAX));
        }

        if (tableOptionList != null) {
            mainCriteria.and("table.tableOptionList").all(tableOptionList);
        }
        if (seating != null) {
            mainCriteria.and("table.seating").gte(seating);
        }

        SortOperation sortByTable = Aggregation.sort(Sort.Direction.ASC, "table._id").and(Sort.Direction.ASC, "reservedStartTime");

        MatchOperation matchOperation = Aggregation.match(mainCriteria);

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sortByTable);

        return mongoTemplate.aggregate(aggregation, "reserved", Reserved.class).getMappedResults();
    }

    @Override
    public List<String> findCafeByReserved(List<String> cafes, LocalDateTime startTime, LocalDateTime endTime, Integer seating, List<TableClass.TableOption> tableOptionList) {

        Map<String, Set> filterCafes = new HashMap<>();
        Criteria mainCriteria = new Criteria();

        if (cafes != null) {
            mainCriteria.and("cafeId").in(cafes);
        }
        if (tableOptionList != null) {
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
            Criteria case1Criteria = Criteria.where("reservedStartTime").gte(startTime).lt(endTime);
            Criteria case2Criteria = Criteria.where("reservedEndTime").gt(startTime).lte(endTime);
            Criteria case3Criteria = Criteria.where("reservedStartTime").lt(startTime).and("reservedEndTime").gt(endTime);

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
    public boolean isReservedByTableIdBetweenTime(String tableId, LocalDateTime startTime, LocalDateTime endTime) {

        Criteria criteria1 = Criteria.where("reservedStartTime").gte(startTime).lt(endTime);
        Criteria criteria2 = Criteria.where("reservedEndTime").gt(startTime).lte(endTime);
        Criteria criteria3 = Criteria.where("reservedStartTime").lt(startTime).and("reservedEndTime").gt(endTime);

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
        Criteria tableLimitCriteria = Criteria.where("reservedStartTime").lte(tableEndTime).and("reservedEndTime").gte(tableStartTime);
        Criteria criteria1 = Criteria.where("reservedStartTime").gte(startTime).lt(endTime);
        Criteria criteria2 = Criteria.where("reservedEndTime").gt(startTime).lte(endTime);
        Criteria criteria3 = Criteria.where("reservedStartTime").lt(startTime).and("reservedEndTime").gt(endTime);

        AggregationOperation sort1 = Aggregation.sort(Sort.Direction.ASC, "tableId");
        AggregationOperation sort2 = Aggregation.sort(Sort.Direction.ASC, "reservedStartTime");
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(tableLimitCriteria),
                Aggregation.match(criteria1),
                Aggregation.match(criteria2),
                Aggregation.match(criteria3),
                Aggregation.lookup("tableClass", "id", "tableClassId", "tableClass"),
                Aggregation.lookup("cafe", "id", "cafeId", "cafe"),
                Aggregation.project("id", "userId", "cafeId", "tableId", "reservedStartTime", "reservedEndTime")
                        .andExpression("tableClass").arrayElementAt(0).as("tableClass"),
                sort1,
                sort2
        );

        return mongoTemplate.aggregate(aggregation, Reserved.class, Reserved.class).getMappedResults();
    }


    @Override
    public List<Reserved> findTodayReservedById(String cafeId) {
        LocalDateTime today = LocalDateTime.now();

        MatchOperation matchToday = Aggregation.match(Criteria.where("reservedStartTime").gte(today.with(LocalTime.MIN)).lt(today.with(LocalTime.MAX)));

        MatchOperation matchCafdId = Aggregation.match(Criteria.where("cafeId").is(cafeId));

        SortOperation sortByTable = Aggregation.sort(Sort.Direction.ASC, "table._id").and(Sort.Direction.ASC, "reservedStartTime");

        Aggregation aggregation = Aggregation.newAggregation(matchToday, matchCafdId, sortByTable);

        return mongoTemplate.aggregate(aggregation, "reserved", Reserved.class).getMappedResults();
    }
}
