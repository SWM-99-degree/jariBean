package com.example.jariBean.repository.matching;

import com.example.jariBean.entity.Matching;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchingRepositoryImpl implements MatchingRepositoryTemplate {
    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public Optional<List<Matching>> findMatchingProgress(String cafeId) {
        Criteria criteria = Criteria.where("cafe._id").is(cafeId).and("status").is(Matching.Status.PROCESSING);
        Query query = new Query(criteria);
        return Optional.of(mongoTemplate.find(query, Matching.class));
    }

    @Override
    public List<String> findCafeIdSortedByCount(Pageable pageable) {
        GroupOperation groupOperation = Aggregation.group("cafeId").count().as("count");
        SortOperation sortByCountDesc = Aggregation.sort(Sort.Direction.DESC, "count");
        Aggregation aggregation = Aggregation.newAggregation(groupOperation, sortByCountDesc);

        Aggregation pageAggregation = Aggregation.newAggregation(
                groupOperation,
                sortByCountDesc,
                Aggregation.skip(pageable.getOffset()),
                Aggregation.limit(pageable.getPageSize())
        );

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
