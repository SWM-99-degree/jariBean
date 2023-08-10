package com.example.jariBean.util;

public class CustomMessageUtil {



    public String cancelMatching(String cafeName) {
        return cafeName + "과의 매칭이 취소되었습니다.";
    }

    public String temMinuteLeft(String cafeName) {
        return cafeName + "과의 예약 시간이 10분 남았습니다. 자리를 정돈해주시길 바랍니다.";
    }

    public String checkTodayReserve(String cafeName) {
        return cafeName + "님, 오늘의 예약을 확인하세요!";
    }

    public String clearMatching(String cafeName) {
        return cafeName + "과의 매칭이 완료되었습니다!";
    }

    public String clearReserveforUser(String cafeName, String time) {
        return cafeName + "의 예약이 " + time + "에 시작될 예정입니다.";
    }

    public String clearReserveforCafe() {
        return null;
    }

}