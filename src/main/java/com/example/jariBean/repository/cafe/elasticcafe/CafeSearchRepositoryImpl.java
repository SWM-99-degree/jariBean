package com.example.jariBean.repository.cafe.elasticcafe;

import com.example.jariBean.entity.elasticentity.Cafe;
import com.example.jariBean.repository.cafe.CafeRepositoryTemplate;
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
public class CafeSearchRepositoryImpl implements CafeSearchRepositoryTemplate {

    private final ElasticsearchOperations operations;

    // TODO pageable 필요함
    @Override
    public List<String> findBySearchingWord(String searchCondition) {
        if (searchCondition == null) {
            return null;
        }

        CriteriaQuery query = createConditionCriteriaQuery(searchCondition);
                //.setPageable(pageable);
        SearchHits<Cafe> search = operations.search(query, Cafe.class);

        return search.stream()
                .map(SearchHit::getId)
                .toList();
    }

    private CriteriaQuery createConditionCriteriaQuery(String searchCondition) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());
        // 검색어로 filter
        //
        // 시간 table을 모두 꺼내서 각각 비교해야 함
        if (searchCondition == null)
            return query;

        query.addCriteria(Criteria.where("text").is(searchCondition));

        return query;
    }
}