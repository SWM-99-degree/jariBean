package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.reserved.ReserveReqDto.ReserveSaveReqDto;
import com.example.jariBean.dto.reserved.ReservedResDto.ReserveSummaryResDto;
import com.example.jariBean.service.ReserveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reserve")
public class ReserveController {

    private final ReserveService reserveService;

    @Operation(summary = "find reserve list", description = "api for find reserve list")
    @ApiResponse(
            responseCode = "200",
            description = "예약 내역 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReserveSummaryResDto.class)))
    )
    @GetMapping
    public ResponseEntity reserveList(@AuthenticationPrincipal LoginUser loginUser, Pageable pageable) {
        Page<ReserveSummaryResDto> reserveSummaryResDtoList = reserveService.getMyReserved(loginUser.getUser().getId(), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다", reserveSummaryResDtoList), OK);
    }

    @Operation(summary = "save reserve", description = "api for save reserve")
    @ApiResponse(
            responseCode = "201",
            description = "예약 내역 저장 성공",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @PostMapping
    public ResponseEntity saveReserve(@AuthenticationPrincipal LoginUser loginUser,@Validated @RequestBody ReserveSaveReqDto reserveSaveReqDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), BAD_REQUEST);
        }
        reserveService.saveReserved(loginUser.getUser().getId(), reserveSaveReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "정보가 성공적으로 등록되었습니다.", null), CREATED);
    }

    @Operation(summary = "delete reserve", description = "api for delete reserve")
    @ApiResponse(
            responseCode = "200",
            description = "예약 내역 삭제 성공",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @DeleteMapping("/{id}")
    public ResponseEntity deleteReserve(@PathVariable String id) {
        reserveService.deleteMyReserved(id);
        return new ResponseEntity<>(new ResponseDto<>(1, "정보가 성공적으로 삭제되었습니다.", null), OK);
    }


}
