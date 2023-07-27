package com.example.jariBean.service;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.cafe.elasticcafe.CafeSearchRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final CafeSearchRepository cafeSearchRepository;

    private final ReservedRepository reservedRepository;

    private final CafeRepository cafeRepository;

    @Transactional
    public List<Cafe> findByText(String text, LocalDateTime startTime, LocalDateTime endTime, List<TableClass.TableOption> tableOptionsList){
        List<String> wordFilterdCafes = cafeSearchRepository.findByTextLike(text);

        List<String> optionsFilterdCafes = reservedRepository.findCafeByReserved(wordFilterdCafes, startTime, endTime, tableOptionsList);

        List<Cafe> cafes = cafeRepository.findById(optionsFilterdCafes);


        return cafes;
    }
}
