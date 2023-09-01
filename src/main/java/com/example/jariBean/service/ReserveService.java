package com.example.jariBean.service;

import com.example.jariBean.dto.reserved.ReservedResDto.NearestReservedResDto;
import com.example.jariBean.dto.reserved.ReservedResDto.ReserveSummaryResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.handler.ex.CustomNoContentException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.jariBean.dto.reserved.ReserveReqDto.ReserveNearestReqDto;
import static com.example.jariBean.dto.reserved.ReserveReqDto.ReserveSaveReqDto;

@Service
@RequiredArgsConstructor
public class ReserveService {

    @Autowired
    private ReservedRepository reservedRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private TableRepository tableRepository;

    public Page<ReserveSummaryResDto> getMyReserved(String userId, Pageable pageable) {

        Page<Reserved> reservedList = reservedRepository.findByUserIdOrderByStartTimeAsc(userId, pageable);
        Page<ReserveSummaryResDto> reserveSummaryResDtoList = reservedList.map(reserved -> new ReserveSummaryResDto(reserved));

        for (ReserveSummaryResDto reserveSummaryResDto : reserveSummaryResDtoList) {
            System.out.println(reserveSummaryResDto.getReserveStartTime());
        }

        return reserveSummaryResDtoList;
    }

    // 예약 삭제하기
    public void deleteMyReserved(String reservedId) {
        try {
            reservedRepository.deleteById(reservedId);
        } catch (Exception e) {
            throw new CustomDBException("해당 데이터는 존재하지 않습니다");
        }
    }


    // 가장 가까운 예약
    public ReserveSummaryResDto getNearestReserved(String userId) {
        LocalDateTime userNow = LocalDateTime.now();

        Reserved reserved = reservedRepository.findNearestReserved(userId, userNow);

        // 정보가 없다면 null 값으로 반환하며, 예외처리로 204를 보냄
        if (reserved == null) {
            throw new CustomNoContentException("예약이 존재하지 않습니다.");
        }

        // 예약 정보 넣기
        ReserveSummaryResDto reserveSummaryResDto = new ReserveSummaryResDto(reserved);
        return reserveSummaryResDto;
    }


    // 예약 신청
    @Transactional
    public void saveReserved(String userId, ReserveSaveReqDto saveReservedReqDto) {
        // 검증해야 할 테이블의 예약되어 있는 것들 중, 당일에 있는 것을 가져옴
        // 만약에 있으면 삭제
        boolean isExist = reservedRepository.isReservedByTableIdBetweenTime(saveReservedReqDto.getTableId(), saveReservedReqDto.getReservedStartTime(), saveReservedReqDto.getReservedEndTime());
        if (isExist){
            throw new CustomDBException("데이터가 중복됩니다.");
        }

        // 예약에 대한 처리
        Cafe cafe = cafeRepository.findById(saveReservedReqDto.getCafeId()).orElse(null);

        Table table = tableRepository.findById(saveReservedReqDto.getTableId()).orElse(null);

        Reserved reserved = saveReservedReqDto.toEntity(userId, table, cafe);
        reservedRepository.save(reserved);
    }

}
