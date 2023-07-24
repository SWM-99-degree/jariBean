package com.example.jariBean.repository.cafe.elasticcafe;

import com.example.jariBean.entity.elasticentity.Cafe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CafeSearchRepository extends ElasticsearchRepository<Cafe, String> {

    List<Cafe> findBySearchingWord(String word);

}
