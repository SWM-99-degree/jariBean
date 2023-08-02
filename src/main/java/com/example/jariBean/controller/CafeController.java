package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.cafe.CafeReqDto.CafeSearchReqDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailReserveDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.service.CafeService;
import com.example.jariBean.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe")
public class CafeController {

    private final SearchService searchService;
    private final CafeService cafeService;



    @GetMapping("/best")
    public ResponseEntity bestCafe() {
        List<CafeSummaryDto> cafeSummaryDtos = cafeService.getCafeByMatchingCount();

        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다.", cafeSummaryDtos), CREATED);
    }
    @GetMapping("/{cafeId}")
    public ResponseEntity moreInfo(@PathVariable("cafeId") String cafeId) {
        CafeDetailReserveDto cafeDetailReserveDto = cafeService.getCafeWithTodayReserved(cafeId);

        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다", cafeDetailReserveDto), CREATED);
    }

    @PostMapping
    public List<CafeSummaryDto> cafes(@RequestBody CafeSearchReqDto cafeSearchReqDto) {
        List<Cafe> cafes = searchService.findByText(
                cafeSearchReqDto.getSearchingWord(),
                cafeSearchReqDto.getLocation().getLatitude(),
                cafeSearchReqDto.getLocation().getLongitude(),
                cafeSearchReqDto.getReserveStartTime(),
                cafeSearchReqDto.getReserveEndTime(),
                cafeSearchReqDto.getPeopleNumber(),
                cafeSearchReqDto.getTableOptionList()
        );

        List<CafeSummaryDto> cafeSummaryDtos = new ArrayList<>();
        cafes.forEach(cafe -> cafeSummaryDtos.add(new CafeSummaryDto(cafe)));
        return cafeSummaryDtos;
    }

}
