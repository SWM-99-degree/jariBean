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
     * 최기성
     * [검색결과-테이블] 에서 예약 가능한 테이블의 예약내역을 가져오는 로직
     * @param reservedTableListReqDto
     * @return {
     *  "cafeName" : "카페 이름",
     *  "cafeImg" : "이미지 url",
     *  "table" : [(table 번호, table class, table img,[사용시간])]
     * }
     */
    public ReservedTableListResDto findReservedListByCafeId(ReserveTableListReqDto reservedTableListReqDto) {
        // 카페의 운영 시간 확인
        LocalDateTime standardTime = reservedTableListReqDto.getStartTime();
        Cafe cafe = cafeRepository.findByIdwithOperatingTime(reservedTableListReqDto.getCafeId());
        LocalDateTime openTime = cafe.getCafeOperatingTimeList().get(0).getOpenTime().withYear(standardTime.getYear()).withMonth(standardTime.getMonthValue()).withDayOfMonth(standardTime.getDayOfMonth());
        LocalDateTime closeTime = cafe.getCafeOperatingTimeList().get(0).getCloseTime().withYear(standardTime.getYear()).withMonth(standardTime.getMonthValue()).withDayOfMonth(standardTime.getDayOfMonth());

        // +1시간 넓히는 단위에 운영시간을 침범하는 경우를 계산
        if (Duration.between(reservedTableListReqDto.getStartTime().minusHours(1L), openTime).toMinutes() > 0) {
        } else {openTime = reservedTableListReqDto.getStartTime().minusHours(1L);}

        if (Duration.between(closeTime, reservedTableListReqDto.getEndTime().plusHours(1L)).toMinutes() > 0) {
        } else {closeTime = reservedTableListReqDto.getEndTime().plusHours(1L);}


        // 예약 내역 가져오기 및 카페 내용 가져오기
        List<Reserved> reservedList = reservedRepository.findReservedByIdBetweenTime(reservedTableListReqDto.getCafeId(), reservedTableListReqDto.getStartTime(), reservedTableListReqDto.getEndTime());

        LocalDateTime endTime = null;
        String tableId = "";

        // 초기화
        List<ReservedTableListResDto.TimeTable.ReservingTime> reservingTimes = new ArrayList<>();
        ReservedTableListResDto.TimeTable timeTable = new ReservedTableListResDto.TimeTable();
        ReservedTableListResDto reservedTableListResDto = new ReservedTableListResDto();

        // 카페 정보
        reservedTableListResDto.setCafeImg(cafe.getCafeImg());
        reservedTableListResDto.setCafeName(cafe.getName());
        boolean flag = false;
        for (Reserved reserved : reservedList) {
            // 만약 테이블id가 달라지게 된다면 형성된 테이블의 정보를 넣고, 새로운 테이블의 정보를 구성한다.
            if (!tableId.equals(reserved.getTable().getId())){
                if (flag == Boolean.TRUE) {
                    reservingTimes.add(new ReservedTableListResDto.TimeTable.ReservingTime(endTime, closeTime));
                    timeTable.setReservingTimes(reservingTimes);
                    reservedTableListResDto.appendTimetable(timeTable);
                }
                flag = true;

                // 초기화
                reservingTimes = new ArrayList<>();
                timeTable = new ReservedTableListResDto.TimeTable();
                tableId = reserved.getTable().getId();
                endTime = reserved.getReservedEndTime();
                ReservedTableListResDto.TimeTable.ReservingTime reservingTime = new ReservedTableListResDto.TimeTable.ReservingTime(openTime, reserved.getReservedStartTime());
                // 테이블의 대한 정보 입력
                timeTable.setTableOptions(reserved.getTableClass().getTableOptionList());
                timeTable.setTableId(tableId);
                reservingTimes.add(reservingTime);
            } else {
                if (reserved.getReservedStartTime().equals(endTime)) {
                    endTime = reserved.getReservedEndTime();
                } else {
                    reservingTimes.add(new ReservedTableListResDto.TimeTable.ReservingTime(endTime, reserved.getReservedStartTime()));
                    endTime = reserved.getReservedEndTime();
                }
            }
        }
        reservingTimes.add(new ReservedTableListResDto.TimeTable.ReservingTime(endTime, closeTime));
        timeTable.setReservingTimes(reservingTimes);
        reservedTableListResDto.appendTimetable(timeTable);

        return reservedTableListResDto;

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
                Cafe cafe = cafeRepository.findById(saveReservedReqDto.getCafeId()).orElseThrow();
                Table table = tableRepository.findById(saveReservedReqDto.getTableId()).orElseThrow();
                Reserved reserved = saveReservedReqDto.toEntity(userId, table, cafe);
                reservedRepository.save(reserved);
            } catch (Exception e) {
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
