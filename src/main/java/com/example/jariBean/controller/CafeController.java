package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.cafe.CafeReqDto.CafeSearchReqDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailReserveDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.service.CafeService;
import com.example.jariBean.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe")
public class CafeController {

    private final SearchService searchService;
    private final CafeService cafeService;


    @Operation(summary = "find best cafe", description = "api for find best cafe")
    @ApiResponse(
            responseCode = "200",
            description = "핫플레이스 카페 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CafeSummaryDto.class)))
    )
    @GetMapping("/best")
    public ResponseEntity bestCafe(Pageable pageable) {
        Page<CafeSummaryDto> cafeSummaryDtos = cafeService.getCafeByMatchingCount(pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다.", cafeSummaryDtos), OK);
    }

    // 핫플레이스 경로 카페 상세 확인
    @Operation(summary = "find cafe more information", description = "api for find cafe more information")
    @ApiResponse(
            responseCode = "200",
            description = "카페 상세 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = CafeDetailReserveDto.class))
    )
    @GetMapping("/{cafeId}")
    public ResponseEntity moreInfo(@PathVariable("cafeId") String cafeId, Pageable pageable) {
        CafeDetailReserveDto cafeDetailReserveDto = cafeService.getCafeWithTodayReserved(cafeId, pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다", cafeDetailReserveDto), OK);
    }

    // 검색 이후 카페 상세 확인
    @Operation(summary = "find cafe more information", description = "api for find cafe more information")
    @ApiResponse(
            responseCode = "200",
            description = "카페 상세 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = CafeDetailReserveDto.class))
    )
    @GetMapping("/{cafeid}/aftersearch")
    public ResponseEntity moreInfoWithSearch(@PathVariable("cafeid") String cafeId, @RequestParam("starttime") String startTime, @RequestParam("endtime") String endTime, @RequestParam("tableoptions") List<TableClass.TableOption> tableOptions, @RequestParam int peopleNumber, Pageable pageable){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime parsedStartTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime parsedEndTime = LocalDateTime.parse(endTime, formatter);
        CafeDetailReserveDto cafeDetailReserveDto = cafeService.getCafeWithSearchingReserved(cafeId, parsedStartTime, parsedEndTime, peopleNumber, tableOptions, pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다", cafeDetailReserveDto), OK);
    }

    @Operation(summary = "search cafe list", description = "api for search cafe list")
    @ApiResponse(
            responseCode = "201",
            description = "카페 검색 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CafeSummaryDto.class)))
    )
    @PostMapping
    public ResponseEntity cafes(@RequestBody CafeSearchReqDto cafeSearchReqDto, Pageable pageable) {
        Page<Cafe> cafes = searchService.findByText(cafeSearchReqDto, pageable);
        Page<CafeSummaryDto> cafeSummaryDtos = cafes.map(cafe -> new CafeSummaryDto(cafe));

        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다", cafeSummaryDtos), CREATED);
    }

    @Operation(summary = "cafe summary information", description = "api for search cafe list")
    @ApiResponse(
            responseCode = "200",
            description = "카페 간략 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = CafeSummaryDto.class))
    )
    @GetMapping("/{cafeId}/summary")
    public ResponseEntity cafeSummaryInfo(@PathVariable("cafeId") String cafeId) {
        CafeSummaryDto cafeSummaryDto = cafeService.getSummaryByCafeId(cafeId);
        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다", cafeSummaryDto), OK);
    }

}
