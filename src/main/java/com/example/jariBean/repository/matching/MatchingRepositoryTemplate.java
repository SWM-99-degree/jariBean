package com.example.jariBean.repository.matching;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchingRepositoryTemplate {



    List<String> findCafeIdSortedByCount(Pageable pageable);
}
