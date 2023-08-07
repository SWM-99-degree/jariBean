package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.manager.ManagerReqDto.ManagerTableClassReqDto;
import com.example.jariBean.dto.manager.ManagerReqDto.ManagerTableReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ManagerController {

    @PutMapping("/matching")
    public ResponseEntity matching() {
        return new ResponseEntity<>(new ResponseDto<>(1, "매칭 상태 변경 완료", null), OK);
    }

    @GetMapping("/reserve")
    public ResponseEntity reservePage() {
        return new ResponseEntity<>(new ResponseDto<>(1, "예약 page 결과", null), OK);
    }

    @GetMapping("/table")
    public ResponseEntity tablePage() {
        return new ResponseEntity<>(new ResponseDto<>(1, "좌석 page 결과", null), OK);
    }

    @PutMapping("/table/{tableId}")
    public ResponseEntity updateTable(@PathVariable("tableId") String tableId,
                            @RequestBody ManagerTableReqDto managerTableReqDto) {
        return new ResponseEntity<>(new ResponseDto<>(1, "매칭 상태 변경 완료", null), OK);
    }

    @PostMapping("/table")
    public ResponseEntity addTable(@RequestBody ManagerTableReqDto managerTableReqDto) {
        return new ResponseEntity<>(new ResponseDto<>(1, "매칭 상태 변경 완료", null), CREATED);
    }

    @PutMapping("/tableclass/{tableClassId}")
    public ResponseEntity updateTableClass(@PathVariable("tableClassId") String tableClassId,
                                 @RequestBody ManagerTableClassReqDto managerTableClassReqDto) {
        return new ResponseEntity<>(new ResponseDto<>(1, "매칭 상태 변경 완료", null), OK);
    }

    @PostMapping("/tableclass")
    public ResponseEntity addTableClass(@RequestBody ManagerTableClassReqDto managerTableClassReqDto) {
        return new ResponseEntity<>(new ResponseDto<>(1, "매칭 상태 변경 완료", null), CREATED);
    }

}
