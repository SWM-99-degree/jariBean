package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.manager.ManagerReqDto.ManagerJoinReqDto;
import com.example.jariBean.dto.manager.ManagerReqDto.ManagerTableClassReqDto;
import com.example.jariBean.dto.manager.ManagerReqDto.ManagerTableReqDto;
import com.example.jariBean.dto.manager.ManagerResDto.ManagerLoginResDto;
import com.example.jariBean.dto.manager.ManagerResDto.ReserveDto;
import com.example.jariBean.dto.manager.ManagerResDto.TableClassDto;
import com.example.jariBean.dto.manager.ManagerResDto.TableDto;
import com.example.jariBean.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    @Operation(summary = "join manager", description = "api for join manager")
    @ApiResponse(
            responseCode = "201",
            description = "점주 회원가입 성공",
            content = @Content(schema = @Schema(implementation = ManagerLoginResDto.class))
    )
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody ManagerJoinReqDto managerJoinReqDto) {
        ManagerLoginResDto managerLoginResDto = managerService.join(managerJoinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 완료", managerLoginResDto), CREATED);
    }

    @Operation(summary = "toggle matching status", description = "api for toggle matching status")
    @ApiResponse(
            responseCode = "200",
            description = "매칭 상태 변경",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @PutMapping("/matching/{cafeId}")
    public ResponseEntity matching(@PathVariable("cafeId") String cafeId) {
        managerService.toggleMatchingStatus(cafeId);
        return new ResponseEntity<>(new ResponseDto<>(1, "매칭 상태 변경 완료", null), OK);
    }

    @Operation(summary = "find table-class list", description = "api for find table-class list")
    @ApiResponse(
            responseCode = "200",
            description = "테이블 클래스 데이터 조회",
            content = @Content(schema = @Schema(implementation = TableClassDto.class))
    )
    @GetMapping("/table-class/{cafeId}")
    public ResponseEntity findTableClass(@PathVariable("cafeId") String cafeId) {
        List<TableClassDto> tableClassList = managerService.getTableClassList(cafeId);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 클래스 정보 조회 성공", tableClassList), OK);
    }

    @Operation(summary = "find table list", description = "api for find table list")
    @ApiResponse(
            responseCode = "200",
            description = "테이블 데이터 조회",
            content = @Content(schema = @Schema(implementation = TableDto.class))
    )

    @GetMapping("/table/{tableClassId}")
    public ResponseEntity findTable(@PathVariable("tableClassId") String tableClassId) {
        List<TableDto> tableList = managerService.getTableList(tableClassId);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 정보 조회 성공", tableList), OK);
    }

    @Operation(summary = "find reserve list", description = "api for find reserve list")
    @ApiResponse(
            responseCode = "200",
            description = "예약 내역 조회",
            content = @Content(schema = @Schema(implementation = ReserveDto.class))
    )
    @GetMapping("/reserve/{tableClassId}")
    public ResponseEntity findReserveList(@PathVariable("tableClassId") String tableClassId) {
        Map<String, List<ReserveDto>> reserveList = managerService.getReserveList(tableClassId);
        return new ResponseEntity<>(new ResponseDto<>(1, "예약 내역 조회 성공", reserveList), OK);
    }



    @Operation(summary = "update table information", description = "api for update table information")
    @ApiResponse(
            responseCode = "200",
            description = "테이블 정보 갱신",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @PutMapping("/table/{tableId}")
    public ResponseEntity updateTable(@PathVariable("tableId") String tableId,
                            @RequestBody ManagerTableReqDto managerTableReqDto) {
        managerService.updateTable(tableId, managerTableReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 정보 수정 완료", null), OK);
    }

    @Operation(summary = "add table information", description = "api for add table information")
    @ApiResponse(
            responseCode = "200",
            description = "테이블 추가",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @PostMapping("/table")
    public ResponseEntity addTable(@RequestBody ManagerTableReqDto managerTableReqDto) {
        managerService.addTable(managerTableReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 추가 완료", null), CREATED);
    }

    @Operation(summary = "update table class information", description = "api for update table class information")
    @ApiResponse(
            responseCode = "200",
            description = "테이블 클래스 업데이트",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @PutMapping("/tableclass/{tableClassId}")
    public ResponseEntity updateTableClass(@PathVariable("tableClassId") String tableClassId,
                                 @RequestBody ManagerTableClassReqDto managerTableClassReqDto) {
        managerService.updateTableClass(tableClassId, managerTableClassReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 클래스 정보 수정 완료", null), OK);
    }

    @Operation(summary = "add table class information", description = "api for add table class information")
    @ApiResponse(
            responseCode = "200",
            description = "테이블 클래스 추가",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @PostMapping("/tableclass/{cafeId}")
    public ResponseEntity addTableClass(@PathVariable("cafeId") String cafeId,
                                        @RequestBody ManagerTableClassReqDto managerTableClassReqDto) {
        managerService.addTableClass(cafeId, managerTableClassReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 클래스 추가 완료", null), CREATED);
    }

}
