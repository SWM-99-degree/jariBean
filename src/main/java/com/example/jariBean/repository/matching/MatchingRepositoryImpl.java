package com.example.jariBean.repository.matching;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;

import java.util.ArrayList;
import java.util.List;

public class MatchingRepositoryImpl implements MatchingRepositoryTemplate {
    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public List<String> findCafeIdSortedByCount() {
        GroupOperation groupOperation = Aggregation.group("cafeId").count().as("matchingCount");
        SortOperation sortByCountDesc = Aggregation.sort(Sort.Direction.DESC, "count");
        Aggregation aggregation = Aggregation.newAggregation(groupOperation, sortByCountDesc);

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
