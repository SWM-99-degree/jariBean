package com.example.jariBean.service;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Table;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private ReservedRepository reservedRepository;
    private CafeRepository cafeRepository;
    private TableRepository tableRepository;
    private UserRepository userRepository;

    // 손님 앱

    // 가장 가까운 예약
    public Object getNearestReserve(){
        return null;
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
    public void findReservedListByCafeId(String cafeId) {

    }

    /**
     * 예약하기 로직
     * @param cafeId
     * @param UserId
     * @param tableId
     * @param startTime
     * @param endTime
     */
    // 예약 신청
    public void saveReserved(String cafeId, String UserId, String tableId, String startTime, String endTime) {

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
