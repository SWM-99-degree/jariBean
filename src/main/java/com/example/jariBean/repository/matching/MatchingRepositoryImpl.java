package com.example.jariBean.repository.matching;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;

import java.util.ArrayList;
import java.util.List;

public class MatchingRepositoryImpl implements MatchingRepositoryTemplate {
    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public List<String> findCafeIdSortedByCount(Pageable pageable) {
        GroupOperation groupOperation = Aggregation.group("cafeId").count().as("matchingCount");
        SortOperation sortByCountDesc = Aggregation.sort(Sort.Direction.DESC, "count");
        SkipOperation skipOperation = Aggregation.skip(pageable.getOffset());
        LimitOperation limitOperation = Aggregation.limit(pageable.getPageSize());
        Aggregation aggregation = Aggregation.newAggregation(groupOperation, sortByCountDesc, skipOperation, limitOperation);

        AggregationResults<CafeCount> results = mongoTemplate.aggregate(aggregation, "matching", CafeCount.class);

        List<String> cafeList = new ArrayList<>();

        results.getMappedResults().forEach(result -> cafeList.add(result.getCafeId()));

        return cafeList;
    }

    @Getter
    class CafeCount {
        private String cafeId;
        private int count;

        // Constructors, getters, and setters
    }
}
