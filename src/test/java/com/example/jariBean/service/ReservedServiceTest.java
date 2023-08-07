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
        String reservedId = "64d09fd8520d313df06257e7";

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



}