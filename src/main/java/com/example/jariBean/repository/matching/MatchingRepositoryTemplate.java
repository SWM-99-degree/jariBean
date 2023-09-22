package com.example.jariBean.repository.matching;

import com.example.jariBean.entity.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MatchingRepositoryTemplate {


    Optional<List<Matching>> findMatchingProgress(String cafeId);
    List<String> findCafeIdSortedByCount(Pageable pageable);
}
