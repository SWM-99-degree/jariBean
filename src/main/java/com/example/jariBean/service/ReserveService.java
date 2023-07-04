package com.example.jariBean.service;

import com.example.jariBean.dto.dbconnect.CafeJoinOperatingTimeDto;
import com.example.jariBean.dto.dbconnect.ReservedJoinTableDto;
import com.example.jariBean.dto.reserved.ReservedReqDto;
import com.example.jariBean.dto.reserved.ReservedResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.handler.ex.CustomNoContentException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReserveService {


    private final ReservedRepository reservedRepository;
    private final CafeRepository cafeRepository;

    // 손님 앱

    // 가장 가까운 예약
    public ReservedResDto.NearestReservedResDto getNearestReserved(ReservedReqDto.NearestReservedReqDto nearestReservedReqDto){

        String userId = nearestReservedReqDto.getUserId();
        LocalDateTime userNow = nearestReservedReqDto.getUserNow();
        // 예약정보
        Reserved reserved = reservedRepository.findNearestReserved(userId, userNow);
        System.out.println(reserved.getCafe());
        // 정보가 없다면 null 값으로 반환하며, 예외처리로 204를 보냄
        if (reserved == null) { throw new CustomNoContentException("예약이 존재하지 않습니다.");}
        // 예약 정보 넣기
        ReservedResDto.NearestReservedResDto reservedResDto = new ReservedResDto.NearestReservedResDto(userNow, reserved);
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
    public ReservedResDto.ReservedTableListResDto findReservedListByCafeId(ReservedReqDto.ReservedTableListReqDto reservedTableListReqDto) {
        // 예약 내역 가져오기 및 카페 내용 가져오기
        List<Reserved> reservedList = reservedRepository.findReservedByIdBetweenTime(reservedTableListReqDto.getUserId(), reservedTableListReqDto.getUserNow());
        Cafe cafe = cafeRepository.findByIdwithOperatingTime(reservedTableListReqDto.getUserId());
        LocalDateTime openTime = cafe.getCafeOperatingTimeList().get(0).getOpenTime()
                .withYear(reservedTableListReqDto.getUserNow().getYear()).withMonth(reservedTableListReqDto.getUserNow().getMonthValue()).withDayOfMonth(reservedTableListReqDto.getUserNow().getDayOfMonth());
        LocalDateTime closeTime = cafe.getCafeOperatingTimeList().get(0).getCloseTime()
                .withYear(reservedTableListReqDto.getUserNow().getYear()).withMonth(reservedTableListReqDto.getUserNow().getMonthValue()).withDayOfMonth(reservedTableListReqDto.getUserNow().getDayOfMonth());
        LocalDateTime endTime = null;
        String tableId = "";

        // 초기화
        List<ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime> reservingTimes = new ArrayList<>();
        ReservedResDto.ReservedTableListResDto.TimeTable timeTable = new ReservedResDto.ReservedTableListResDto.TimeTable();
        ReservedResDto.ReservedTableListResDto reservedTableListResDto = new ReservedResDto.ReservedTableListResDto();

        // 카페 정보
        reservedTableListResDto.setCafeImg(cafe.getCafeImg());
        reservedTableListResDto.setCafeName(cafe.getCafeName());
        boolean flag = false;
        for (Reserved reserved : reservedList) {
            // 만약 테이블id가 달라지게 된다면 형성된 테이블의 정보를 넣고, 새로운 테이블의 정보를 구성한다.
            if (!tableId.equals(reserved.getTableId())){
                if (flag == Boolean.TRUE) {
                    reservingTimes.add(new ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime(endTime, closeTime));
                    timeTable.setReservingTimes(reservingTimes);
                    reservedTableListResDto.appendTimetable(timeTable);
                }
                flag = true;

                // 초기화
                reservingTimes = new ArrayList<>();
                timeTable = new ReservedResDto.ReservedTableListResDto.TimeTable();
                tableId = reserved.getTableId();
                endTime = reserved.getReservedEndTime();
                ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime reservingTime = new ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime(openTime, reserved.getReservedStartTime());
                // 테이블의 대한 정보 입력
                timeTable.setTableOptions(reserved.getTableClass().getTableOptions());
                timeTable.setTableId(tableId);
                reservingTimes.add(reservingTime);
            } else {
                if (reserved.getReservedStartTime().equals(endTime)) {
                    endTime = reserved.getReservedEndTime();
                } else {
                    reservingTimes.add(new ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime(endTime, reserved.getReservedStartTime()));
                    endTime = reserved.getReservedEndTime();
                }
            }
        }
        reservingTimes.add(new ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime(endTime, closeTime));
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
    public void saveReserved(ReservedReqDto.SaveReservedReqDto saveReservedReqDto) {
        // 검증해야 할 테이블의 예약되어 있는 것들 중, 당일에 있는 것을 가져옴
        List<Reserved> reserveds = reservedRepository.findReservedByIdAndTableIdBetweenTime(
                saveReservedReqDto.getCafeId(), saveReservedReqDto.getTableId(), saveReservedReqDto.getReservedStartTime()
        );

        // 검증의 과정 Mongo라서 DB 단에서 하기는 어렵다.
        for (Reserved reserve : reserveds) {
            if (saveReservedReqDto.getReservedEndTime().isBefore(reserve.getReservedStartTime()) ||
                    saveReservedReqDto.getReservedStartTime().isAfter(reserve.getReservedEndTime()) ||
                    saveReservedReqDto.getReservedStartTime().isEqual(reserve.getReservedEndTime()) ||
                    saveReservedReqDto.getReservedEndTime().isEqual(reserve.getReservedStartTime())
            ){
            } else { throw new CustomDBException("데이터가 중복됩니다."); }
        }

        Reserved reserved = saveReservedReqDto.toEntity();
        reservedRepository.save(reserved);
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
