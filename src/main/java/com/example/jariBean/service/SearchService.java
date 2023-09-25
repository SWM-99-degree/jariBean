package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeReqDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Lazy
public class SearchService {


    @Autowired
    private ReservedRepository reservedRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private CafeRepository cafeRepository;

    public List<String> dividedWord(String word){

        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        KomoranResult analyzeResultList = komoran.analyze(word);

        List<String> wordList = new ArrayList<>();

        // divided word in 'N'
        for (Token new_word: analyzeResultList.getTokenList()) {
            if (new_word.getPos().contains("N")){
                wordList.add(new_word.getMorph());
            }
        }

        return wordList;
    }

    public boolean checkingCafeAbleReserve(String cafeId, LocalDateTime reserveStartTime, LocalDateTime reserveEndTime, Integer seating, List<TableClass.TableOption> tableOptions){

        List<Table> tables = tableRepository.findByConditions(cafeId, seating, tableOptions);

        if ((reserveStartTime == null || reserveEndTime == null) && !tables.isEmpty()) {
            return true;
        }
        System.out.println(cafeId);
        tables.forEach(table -> {
            System.out.println(reservedRepository.isReservedByTableIdBetweenTime(table.getId(), reserveStartTime, reserveEndTime));
        });

        boolean isAbleReserve = tables
                .stream()
                .anyMatch(table -> !reservedRepository.isReservedByTableIdBetweenTime(table.getId(), reserveStartTime, reserveEndTime));

        return isAbleReserve;
    }

    @Transactional
    public Page<Cafe> findByText(CafeReqDto.CafeSearchReqDto cafeSearchReqDto, Pageable pageable){
        // for searching
        String text = cafeSearchReqDto.getSearchingWord();
        Double latitude = cafeSearchReqDto.getLocation().getLatitude();
        Double longitude = cafeSearchReqDto.getLocation().getLongitude();
        LocalDateTime startTime = cafeSearchReqDto.getReserveStartTime();
        LocalDateTime endTime = cafeSearchReqDto.getReserveEndTime();
        Integer seating = cafeSearchReqDto.getPeopleNumber();
        List<TableClass.TableOption> tableOptionList = cafeSearchReqDto.getTableOptionList();

        // divide searching words
        List<String> searchingWords = dividedWord(text);
        GeoJsonPoint point = new GeoJsonPoint(longitude, latitude);

        // mongoDB search
        List<String> wordFilteredCafes = cafeRepository.findByWordAndCoordinateNear(searchingWords, point);
        System.out.println(wordFilteredCafes);
        List<String> tableFilteredCafes = wordFilteredCafes
                .stream()
                .filter(cafe -> checkingCafeAbleReserve(cafe, startTime, endTime, seating, tableOptionList))
                .collect(Collectors.toList());
        System.out.println(tableFilteredCafes);
        Page<Cafe> cafes = cafeRepository.findByIds(tableFilteredCafes, pageable);

        return cafes;

    }
}
