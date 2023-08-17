package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.manager.ManagerReqDto.*;
import com.example.jariBean.dto.manager.ManagerResDto.*;
import com.example.jariBean.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    @PutMapping("/matching/{cafeId}")
    public ResponseEntity matching(@PathVariable("cafeId") String cafeId) {
        managerService.toggleMatchingStatus(cafeId);
        return new ResponseEntity<>(new ResponseDto<>(1, "매칭 상태 변경 완료", null), OK);
    }

    @GetMapping("/reserve/{cafeId}")
    public ResponseEntity reservePage(@PathVariable("cafeId") String cafeId) {
        ManagerReserveResDto reserveResDto = managerService.getReservePage(cafeId);
        return new ResponseEntity<>(new ResponseDto<>(1, "예약 page 결과", reserveResDto), OK);
    }

    @GetMapping("/table{cafeId}")
    public ResponseEntity tablePage(@PathVariable("cafeId") String cafeId) {
        List<ManagerTableResDto> tablePage = managerService.getTablePage(cafeId);
        return new ResponseEntity<>(new ResponseDto<>(1, "좌석 page 결과", tablePage), OK);
    }

    @PutMapping("/table/{tableId}")
    public ResponseEntity updateTable(@PathVariable("tableId") String tableId,
                            @RequestBody ManagerTableReqDto managerTableReqDto) {
        managerService.updateTable(tableId, managerTableReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 정보 수정 완료", null), OK);
    }

    @PostMapping("/table")
    public ResponseEntity addTable(@RequestBody ManagerTableReqDto managerTableReqDto) {
        managerService.addTable(managerTableReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 추가 완료", null), CREATED);
    }

    @PutMapping("/tableclass/{tableClassId}")
    public ResponseEntity updateTableClass(@PathVariable("tableClassId") String tableClassId,
                                 @RequestBody ManagerTableClassReqDto managerTableClassReqDto) {
        managerService.updateTableClass(tableClassId, managerTableClassReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 클래스 정보 수정 완료", null), OK);
    }

    @PostMapping("/tableclass/{cafeId}")
    public ResponseEntity addTableClass(@PathVariable("cafeId") String cafeId,
                                        @RequestBody ManagerTableClassReqDto managerTableClassReqDto) {
        managerService.addTableClass(cafeId, managerTableClassReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "테이블 클래스 추가 완료", null), CREATED);
    }

}
