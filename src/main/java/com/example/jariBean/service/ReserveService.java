package com.example.jariBean.service;

import com.example.jariBean.dto.reserved.ReservedReqDto;
import com.example.jariBean.dto.reserved.ReservedResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.CafeOperatingTime;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.handler.ex.CustomDBInsertException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.cafeOperatingTime.CafeOperatingTimeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.tableClass.TableClassRepository;
import com.example.jariBean.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private ReservedRepository reservedRepository;
    private CafeRepository cafeRepository;
    private TableRepository tableRepository;
    private UserRepository userRepository;
    private TableClassRepository tableClassRepository;
    private CafeOperatingTimeRepository cafeOperatingTimeRepository;

    // 손님 앱

    // 가장 가까운 예약
    public ReservedResDto.NearestReservedResDto getNearestReserved(ReservedReqDto.NearestReservedReqDto nearestReservedReqDto){

        String userId = nearestReservedReqDto.getUserId();
        LocalDateTime userNow = nearestReservedReqDto.getUserNow();
        // 예약정보
        Reserved reserved = reservedRepository.findReservedByIdAfterTime(userId, userNow);
        // 정보가 없다면 null 값으로 반환
        if (reserved == null) {return null;}
        // 예약 정보 넣기
        ReservedResDto.NearestReservedResDto reservedResDto = new ReservedResDto.NearestReservedResDto();
        reservedResDto.setReservedStartTime(reserved.getReservedStartTime());
        Duration duration = Duration.between(userNow, reserved.getReservedStartTime());
        reservedResDto.setLeftTime(duration.toMinutes());
        // 카페 이름 넣기
        Cafe cafe = cafeRepository.findById(reserved.getCafeId()).get();
        reservedResDto.setCafeName(cafe.getCafeName());

        reservedResDto.setTableOptions(tableClassRepository.findById(tableRepository.findById(reserved.getTableId())
                        .get().getTableClassId())
                .get()
                .getTableOptions());

        return reservedResDto;
    }

    /**
     * 최기성
     * [검색결과-테이블] 에서 예약 가능한 테이블의 예약내역을 가져오는 로직
     * @param cafeId
     * @return {
     *  "cafeName" : "카페 이름",
     *  "cafeImg" : "이미지 url",
     *  "table" : [(table 번호, table class, table img,[사용시간])]
     * }
     */
    public ReservedResDto.ReservedTableListResDto findReservedListByCafeId(String cafeId, LocalDateTime time) {
        // 초기화
        List<ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime> reservingTimes = new ArrayList<>();
        ReservedResDto.ReservedTableListResDto.TimeTable timeTable = new ReservedResDto.ReservedTableListResDto.TimeTable();
        ReservedResDto.ReservedTableListResDto reservedTableListResDto = new ReservedResDto.ReservedTableListResDto();
        // 예약 내역 가져오기 및 카페 내용 가져오기
        List<Reserved> cafeReserved = reservedRepository.findReservedByIdBetweenTime(cafeId, time);
        Cafe cafe = cafeRepository.findById(cafeId).get();
        CafeOperatingTime cafeOperatingTime = cafeOperatingTimeRepository.findById(cafeId).get();
        LocalDateTime openTime = cafeOperatingTime.getOpenTime();
        LocalDateTime endTime = null;
        String tableId = "";


        //        reservedTableListResDto.setCafeImg(cafe.getCafeAddress());
        reservedTableListResDto.setCafeName(cafe.getCafeName());

        for (Reserved reserved : cafeReserved) {

            if (!tableId.equals(reserved.getTableId())){
                timeTable.setReservingTimes(reservingTimes);
                reservedTableListResDto.setTimeTable(timeTable);
                // 초기화
                reservingTimes = new ArrayList<>();
                timeTable = new ReservedResDto.ReservedTableListResDto.TimeTable();
                tableId = reserved.getTableId();
                endTime = reserved.getReservedEndTime();
                ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime reservingTime = new ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime(openTime, reserved.getReservedStartTime());

                timeTable.setTableOptions(tableClassRepository.findById(tableRepository.findById(reserved.getTableId())
                                .get().getTableClassId())
                        .get()
                        .getTableOptions());
                timeTable.setTableId(tableId);
                reservingTimes.add(reservingTime);
            } else {
                if (reserved.getReservedStartTime().equals(endTime)) {
                    continue;
                } else {
                    reservingTimes.add(new ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime(endTime, reserved.getReservedStartTime()));
                    endTime = reserved.getReservedEndTime();
                }

            }
        }
        timeTable.setReservingTimes(reservingTimes);
        reservedTableListResDto.setTimeTable(timeTable);

        return reservedTableListResDto;

    }

    /**
     * 예약하기 로직
     * @param saveReservedReqDto
     */
    // 예약 신청
    public void saveReserved(ReservedReqDto.SaveReservedReqDto saveReservedReqDto) {
        // 검증해야 할 테이블의 예약 상태
        List<Reserved> reserveds = reservedRepository.findReservedByIdAndTableIdBetweenTime(
                saveReservedReqDto.getCafeId(), saveReservedReqDto.getTableId(), saveReservedReqDto.getReservedStartTime()
        );

        // 검증의 과정
        for (Reserved reserve : reserveds) {
            if (saveReservedReqDto.getReservedEndTime().isBefore(reserve.getReservedStartTime()) ||
            saveReservedReqDto.getReservedStartTime().isAfter(reserve.getReservedEndTime())){
            } else { throw new CustomDBInsertException("데이터가 중복됩니다."); }
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
