package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeReqDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.handler.ex.CustomNoContentException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Lazy
public class SearchService {

//    @Lazy
//    private final CafeSearchRepository cafeSearchRepository;

    private final ReservedRepository reservedRepository;

    private final CafeRepository cafeRepository;

    public List<String> dividedWord(String word){

        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        KomoranResult analyzeResultList = komoran.analyze(word);

        List<String> wordList = new ArrayList<>();

        for (Token new_word: analyzeResultList.getTokenList()) {
            if (new_word.getPos().contains("N")){
                wordList.add(new_word.getMorph());
            }
        }

        return wordList;
    }

    @Transactional
    public List<Cafe> findByText(CafeReqDto.CafeSearchReqDto cafeSearchReqDto, Pageable pageable){
        try {
            String text = cafeSearchReqDto.getSearchingWord();
            Double latitude = cafeSearchReqDto.getLocation().getLatitude();
            Double longitude = cafeSearchReqDto.getLocation().getLongitude();
            LocalDateTime startTime = cafeSearchReqDto.getReserveStartTime();
            LocalDateTime endTime = cafeSearchReqDto.getReserveEndTime();
            Integer seating = cafeSearchReqDto.getPeopleNumber();
            List<TableClass.TableOption> tableOptionList = cafeSearchReqDto.getTableOptionList();

            Set<String> wordSet = new HashSet<>();

            List<String> searchingWords = dividedWord(text);
            GeoJsonPoint point = new GeoJsonPoint(latitude, longitude);
            // TODO
            List<String> wordFilterdCafes = cafeRepository.findByWordAndCoordinateNear(searchingWords, point);
            List<String> optionsFilterdCafes = reservedRepository.findCafeByReserved(wordFilterdCafes, startTime, endTime, seating, tableOptionList);
            List<Cafe> cafes = cafeRepository.findByIds(optionsFilterdCafes, pageable).stream()
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .toList();

            return cafes;
        } catch (Exception e) {
            throw new CustomNoContentException("해당되는 결과가 없습니다.");
        }

    }
}
