package com.example.jariBean.service;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.handler.ex.CustomNoContentException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.cafe.elasticcafe.CafeSearchRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Lazy
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    @Lazy
    private final CafeSearchRepository cafeSearchRepository;

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
    public List<Cafe> findByText(String text, double latitude, double longitude, LocalDateTime startTime, LocalDateTime endTime, Integer seating ,List<TableClass.TableOption> tableOptionsList){
        try {
            Set<String> wordSet = new HashSet<>();

            List<String> searchingWords = dividedWord(text);
            List<String> wordFilterdCafes = cafeSearchRepository.findBySearchingWord(searchingWords, latitude, longitude);
            System.out.println(wordFilterdCafes);
            List<String> optionsFilterdCafes = reservedRepository.findCafeByReserved(wordFilterdCafes, startTime, endTime, seating, tableOptionsList);
            System.out.println(optionsFilterdCafes);
            List<Cafe> cafes = cafeRepository.findByIds(optionsFilterdCafes);

            return cafes;
        } catch (Exception e) {
            throw new CustomNoContentException("해당되는 결과가 없습니다.");
        }

    }
}
