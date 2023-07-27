package com.example.jariBean.repository.cafe.elasticcafe;

import com.example.jariBean.entity.elasticentity.Cafe;

import java.util.List;

public interface CafeSearchRepositoryTemplate {

    List<String> findBySearchingWord(String word);
}
