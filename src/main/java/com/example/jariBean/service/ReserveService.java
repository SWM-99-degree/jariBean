package com.example.jariBean.service;

import com.example.jariBean.dto.reserved.ReservedResDto.ReserveSummaryResDto;
import com.example.jariBean.dto.reserved.ReservedResDto.NearestReservedResDto;
import com.example.jariBean.dto.reserved.ReservedResDto.ReservedTableListResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.handler.ex.CustomNoContentException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.jariBean.dto.reserved.ReserveReqDto.*;

@Service
@RequiredArgsConstructor
public class ReserveService {


    private final ReservedRepository reservedRepository;
    private final CafeRepository cafeRepository;
    private final TableRepository tableRepository;

    // 손님 앱

    // 지금까지 한 예약 가져오기
    public List<ReserveSummaryResDto> getMyReserved(String userId, Pageable pageable) {
        List<ReserveSummaryResDto> reserveSummaryResDtoList = new ArrayList<>();
        try {
            List<Reserved> reservedList = reservedRepository.findByUserIdOrderByReservedStartTimeDesc(userId, pageable);
            reservedList.forEach(reserved -> reserveSummaryResDtoList.add(new ReserveSummaryResDto(reserved)));
        } catch (Exception e) {
            throw new CustomDBException("예약 관련 데이터에 문제가 있습니다.");
        }
        return  reserveSummaryResDtoList;
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
    public NearestReservedResDto getNearestReserved(ReserveNearestReqDto nearestReservedReqDto){
        String userId = nearestReservedReqDto.getUserId();
        LocalDateTime userNow = nearestReservedReqDto.getUserNow();
        // 예약정보
        Reserved reserved = reservedRepository.findNearestReserved(userId, userNow);
        // 정보가 없다면 null 값으로 반환하며, 예외처리로 204를 보냄
        if (reserved == null) { throw new CustomNoContentException("예약이 존재하지 않습니다.");}
        // 예약 정보 넣기
        NearestReservedResDto reservedResDto = new NearestReservedResDto(userNow, reserved);
        return reservedResDto;
    }
    /**
     * 예약하기 로직
     * @param saveReservedReqDto
     */
    // 예약 신청
    @Transactional
    public void saveReserved(String userId, ReserveSaveReqDto saveReservedReqDto) {
        // 검증해야 할 테이블의 예약되어 있는 것들 중, 당일에 있는 것을 가져옴
        boolean isExist = reservedRepository.isReservedByTableIdBetweenTime(saveReservedReqDto.getTableId(), saveReservedReqDto.getReservedStartTime(), saveReservedReqDto.getReservedEndTime());
        if (isExist){
            throw new CustomDBException("데이터가 중복됩니다.");
        } else {
            try {
                Cafe cafe = cafeRepository.findById(saveReservedReqDto.getCafeId()).orElse(null);
                Table table = tableRepository.findById(saveReservedReqDto.getTableId()).orElse(null);
                Reserved reserved = saveReservedReqDto.toEntity(userId, table, cafe);
                reservedRepository.save(reserved);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomDBException("카페와 테이블 데이터에 문제가 존재합니다.");
            }
        }
    }

    // 점주 앱

    // Table class 가져오기
    public Object getTableInformation() {
        // class와 table의 정보를 한꺼번에
        return null;
    }

    // Table 예약 가져오기
    // 날짜를 가져오는 대신, 점주의 "요약" 에서는 당일로 날짜 고정

    /**
     *
     * @param CafeId
     * @param date
     * @param tableClassId
     * @return {
     *
     *   "tableClass" : "테이블 클래스"
     *   "table" : [(table 번호, table class, [사용시간])]
     *
     *   "reservedList" : [(시간, 인구), ...]
     * }
     */
    public Object getReserved(String CafeId, String date, String tableClassId) {
        // class와 table 정보 가져오기
        return null;
    }

}
