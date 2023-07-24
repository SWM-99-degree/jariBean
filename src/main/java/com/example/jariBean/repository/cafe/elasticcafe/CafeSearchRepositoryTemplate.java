package com.example.jariBean.repository.cafe.elasticcafe;

import com.example.jariBean.entity.elasticentity.Cafe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CafeSearchRepositoryTemplate {

    private final ElasticsearchOperations operations;

    // TODO SearchCondition의 경우 DTO로 받아야 함
    public List<Cafe> findByCondition(String searchCondition, Pageable pageable) {
        CriteriaQuery query = createConditionCriteriaQuery(searchCondition).setPageable(pageable);



        SearchHits<Cafe> search = operations.search(query, Cafe.class);

        return search.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    private CriteriaQuery createConditionCriteriaQuery(String searchCondition) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());
        // 검색어로 filter
        //
        // 시간 table을 모두 꺼내서 각각 비교해야 함
        if (searchCondition == null)
            return query;

//        if (searchCondition.getId() != null)
//            query.addCriteria(Criteria.where("id").is(searchCondition.getId()));
//회
//        if(searchCondition.getAge() > 0)
//            query.addCriteria(Criteria.where("age").is(searchCondition.getAge()));
//
//        if(StringUtils.hasText(searchCondition.getName()))
//            query.addCriteria(Criteria.where("name").is(searchCondition.getName()));
//
//        if(StringUtils.hasText(searchCondition.getNickname()))
//            query.addCriteria(Criteria.where("nickname").is(searchCondition.getNickname()));
//
//        if(searchCondition.getZoneId() != null)
//            query.addCriteria(Criteria.where("zone.id").is(searchCondition.getZoneId()));
//
//        if(searchCondition.getStatus() != null)
//            query.addCriteria(Criteria.where("status").is(searchCondition.getStatus()));

        return query;
    }
}