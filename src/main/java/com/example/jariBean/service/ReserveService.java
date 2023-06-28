package com.example.jariBean.service;

import org.springframework.stereotype.Service;

@Service
public class ReserveService {

    // 손님 앱

    // 가장 가까운 예약
    public Object getNearleastReserve(){
        return null;
    }

    // 검색 결과
    public Object getSearchedCafe() {
        return null;
    }

    // 검색한 이후 table
    public Object getSearchedTable() {
        return null;
    }

    // 예약 신청
    public void saveReserved() {
    }


    // 점주 앱

    // Table class 가져오기
    public Object getTableInformation() {
        // class와 table의 정보를 한꺼번에
        return null;
    }

    // Table 예약 가져오기
    // 날짜를 가져오는 대신, 점주의 "요약" 에서는 당일로 날짜 고정
    public Object getReserved() {
        // class와 table 정보 가져오기
        return null;
    }

}
