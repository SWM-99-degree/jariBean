package com.example.jariBean.repository.cafe.elasticcafe;

import com.example.jariBean.entity.elasticentity.Cafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;


public class CafeSearchRepositoryImpl implements CafeSearchRepositoryTemplate {

    @Autowired
    private ElasticsearchOperations operations;

    // TODO pageable 필요함
    @Override
    public List<String> findBySearchingWord(List<String> searchWords, double latitude, double longitude) {
        Criteria wordSearchCriteria = new Criteria("text");
        if (searchWords != null) {
            searchWords.forEach(word -> wordSearchCriteria.or("text").contains(word));
        }

        GeoPoint geoPoint = new GeoPoint(latitude, longitude);
        Criteria geoLocationCriteria = new Criteria("location").within(geoPoint, "0.7km");
        Criteria finalCriteria = geoLocationCriteria.and(wordSearchCriteria);
        Query query = new CriteriaQuery(finalCriteria);
        SearchHits<Cafe> search = operations.search(query, Cafe.class);

        return search.stream()
                .map(SearchHit::getId)
                .toList();
    }

}