package com.example.jariBean.service;

import com.example.jariBean.dto.reserved.ReserveReqDto;
import com.example.jariBean.dto.reserved.ReservedResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
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
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    public void findByWordAndCoordinateNearTest() throws Exception {
        String word1 = "무인";
        String word2 = "인천";
        List<String> words = new ArrayList<>();
        words.add(word1);
        words.add(word2);

        GeoJsonPoint coordinate = new GeoJsonPoint(126.6487782154, 37.4538193246568);
        Double distance = 50D;

        List<String> cafeList = cafeRepository.findByWordAndCoordinateNear(words,coordinate);
        System.out.println("byCoordinateDistance.getContent().size() = " + cafeList.size());
        cafeList.forEach(cafe -> {
            System.out.println(cafe);
        });
    }

    @Test
    public void findByCoordinateNearTest() throws Exception {
        GeoJsonPoint coordinate = new GeoJsonPoint(126.6487782154, 37.4538193246568);
        Double distance = 5000D;

        List<Cafe> cafeList = cafeRepository.findByCoordinateNear(coordinate, distance);
        System.out.println("byCoordinateDistance.getContent().size() = " + cafeList.size());
        cafeList.forEach(cafe -> {
            System.out.println(cafe.getId());
        });
    }


    @Test
    public void saveReservedTest() {
        // given
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

        // when, then 겹친다면 CustomDBException을 내보낼 것이고, 아니라면 저장될 것이다.
        if (reservedRepository.isReservedByTableIdBetweenTime(saveReservedReqDto.getTableId(), newStartTime, newEndTime) ) {
            Assertions.assertThrows(CustomDBException.class, () -> reserveService.saveReserved(userId, saveReservedReqDto));
        } else {
            Assertions.assertDoesNotThrow(()-> reserveService.saveReserved(userId, saveReservedReqDto));
        }

    }


    @Test
    public void delteMyReservedTest() {
        // given
        String userId = "testUser";
        Reserved reserved = reservedRepository.findByUserIdOrderByReservedStartTimeDesc(userId, Pageable.ofSize(1)).get(0);
        String reservedId = reserved.getId();

        // then
        Assertions.assertDoesNotThrow(()-> reserveService.deleteMyReserved(reservedId));
    }

    @Test
    public void getMyReservedTest() {
        // given
        String userId = "testUser";

        // then
        Assertions.assertDoesNotThrow(()-> reserveService.getMyReserved(userId, Pageable.ofSize(1)));

    }

    @Test
    public void getNearestReservedTest() {
        // given
        String userId = "testUser";
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        String startTime = "2023-08-07 10:00:00";
        String checkStartTime = "2023-08-07 12:00:00";
        String checkEndTime = "2023-08-07 14:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime time = LocalDateTime.parse(startTime, formatter);
        ReserveReqDto.ReserveNearestReqDto reserveNearestReqDto = new ReserveReqDto.ReserveNearestReqDto();
        reserveNearestReqDto.setUserId(userId);
        reserveNearestReqDto.setUserNow(time);

        // when
        ReservedResDto.NearestReservedResDto nearestReservedResDto = reserveService.getNearestReserved(reserveNearestReqDto);

        // then
        Assertions.assertEquals(nearestReservedResDto.getReservedStartTime(), LocalDateTime.parse(checkStartTime, formatter));
        Assertions.assertEquals(nearestReservedResDto.getReservedEndTime(), LocalDateTime.parse(checkEndTime, formatter));


    }



}