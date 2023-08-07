package com.example.jariBean.service;

import com.example.jariBean.dto.reserved.ReserveReqDto;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
public class ReservedServiceTest {

    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ReservedRepository reservedRepository;

    @Autowired
    ReserveService reserveService;


    @Test
    public void saveReservedTest() {
        String userId = "testUser";
        ReserveReqDto.ReserveSaveReqDto saveReservedReqDto = new ReserveReqDto.ReserveSaveReqDto();
        saveReservedReqDto.setCafeId("64c45ac3935eb61c140793e7");
        saveReservedReqDto.setTableId("64a021f82ff24a6d8c7bd57c");
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        String startTime = "2023-08-07 12:00:00";
        String endTime = "2023-08-07 14:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime newStartTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime newEndTime = LocalDateTime.parse(endTime, formatter);
        saveReservedReqDto.setReservedStartTime(newStartTime);
        saveReservedReqDto.setReservedEndTime(newEndTime);

        if (reservedRepository.isReservedByTableIdBetweenTime(saveReservedReqDto.getTableId(), newStartTime, newEndTime) ) {
            Assertions.assertThrows(CustomDBException.class, () -> reserveService.saveReserved(userId, saveReservedReqDto));
        } else {
            Assertions.assertDoesNotThrow(()-> reserveService.saveReserved(userId, saveReservedReqDto));
        }

    }

    @Test
    public void delteMyReservedTest() {
        // delete 명령을 해도 오류가 안뜸
        Assertions.assertDoesNotThrow(()-> reserveService.deleteMyReserved("64d09fd8520d313df06257e7"));
    }

    @Test
    public void getMyReservedTest() {
        Assertions.assertDoesNotThrow(()-> reserveService.getMyReserved("testUser", Pageable.ofSize(1)));

    }
//
//    @Test
//    public void getNearestReservedTest() {
//        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
//        Cafe cafe = cafeRepository.findByIdwithOperatingTime("64884c1d65989d25539387b5");
//        System.out.println(cafe.getCafeOperatingTimeList().get(0).getOpenTime());
//        ReserveService reserveService = new ReserveService(reservedRepository, cafeRepository);
//
//        ReservedReqDto.NearestReservedReqDto nearestReservedReqDto = new ReservedReqDto.NearestReservedReqDto();
//        nearestReservedReqDto.setUserId("64884d6065989d25539387b6");
//        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 7, 0);
//        nearestReservedReqDto.setUserNow(dateTime1);
//        ReservedResDto.NearestReservedResDto nearestReservedResDto = reserveService.getNearestReserved(nearestReservedReqDto);
//        System.out.println(nearestReservedResDto);
//    }
//
//    @Test
//    public void saveReservedTest() {
//        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
//        Cafe cafe = cafeRepository.findByCafePhoneNumber("01012341234").orElseThrow();
//        ReserveService reserveService = new ReserveService(reservedRepository, cafeRepository);
//        ReservedReqDto.SaveReservedReqDto saveReservedReqDto = new ReservedReqDto.SaveReservedReqDto();
//        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 8, 0);
//        LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 9, 0);
//        saveReservedReqDto.setReservedStartTime(dateTime1);
//        saveReservedReqDto.setReservedEndTime(dateTime2);
//        saveReservedReqDto.setTableId("64a021f82ff24a6d8c7bd57c");
//        saveReservedReqDto.setUserId(user.getId());
//        saveReservedReqDto.setCafeId(cafe.getId());
//
//        reserveService.saveReserved(saveReservedReqDto);
//    }
//
//    @Test
//    public void findReservedListByCafeIdTest() {
//        Cafe cafe = cafeRepository.findByCafePhoneNumber("01031315656").orElseThrow();
//        ReserveService reserveService = new ReserveService(reservedRepository, cafeRepository);
//        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 7, 0);
//        LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 9, 0);
//        ReservedReqDto.ReservedTableListReqDto reservedTableListReqDto = new ReservedReqDto.ReservedTableListReqDto();
//        reservedTableListReqDto.setCafeId(cafe.getId());
//        reservedTableListReqDto.setStartTime(dateTime1);
//        reservedTableListReqDto.setEndTime(dateTime2);
//        ReservedResDto.ReservedTableListResDto reservedTableListResDto = reserveService.findReservedListByCafeId(reservedTableListReqDto);
//
//        for (ReservedResDto.ReservedTableListResDto.TimeTable timeTable : reservedTableListResDto.getTimeTables()){
//            System.out.println("newTable");
//            for (ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime reservingTime : timeTable.getReservingTimes()){
//                System.out.println("-----");
//                System.out.println(reservingTime.getStartTime());
//                System.out.println(reservingTime.getEndTime());
//            }
//        }
//        System.out.println(reservedTableListResDto.getTimeTables());
//
//    }
}