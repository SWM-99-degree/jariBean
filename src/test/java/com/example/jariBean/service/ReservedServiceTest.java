package com.example.jariBean.service;

import com.example.jariBean.dto.reserved.ReserveReqDto.ReserveSaveReqDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.User;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.jariBean.entity.Reserved.ReservedStatus.VALID;
import static com.example.jariBean.entity.Role.CUSTOMER;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservedServiceTest {

    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ReservedRepository reservedRepository;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    ReserveService reserveService;

    @BeforeAll
    public void saveReservedTest() {
        // given
        String socialId = "kakao_1234";
        String nickname = "기무르따리";

        User user = User.builder()
                .id("test")
                .socialId(socialId)
                .nickname(nickname)
                .role(CUSTOMER)
                .build();
        User savedUser = userRepository.save(user);

        // insert Table
        Table table = Table.builder()
                .id("64a021f82ff24a6d8c7bd57c")
                .name("테이블이예용")
                .tableClassId("123")
                .seating(3)
                .cafeId("64c45ac3935eb61c140793e7")
                .build();

        tableRepository.save(table);

        // insert cafe
        String cafeName = "testCafeName";
        String cafePhoneNumber = "12345678910";
        String cafeAddress = "testCafeAddress";
        GeoJsonPoint jsonPoint = new GeoJsonPoint(12.123123,13.131313);
        Cafe cafe = Cafe.builder()
                .id("64c45ac3935eb61c140793e7")
                .name(cafeName)
                .phoneNumber(cafePhoneNumber)
                .description(" this is so beautiful caffee")
                .address(cafeAddress)
                .coordinate(jsonPoint)
                .build();

        cafeRepository.save(cafe);

        // insert reserved
        String userId = "test";
        ReserveSaveReqDto saveReservedReqDto = new ReserveSaveReqDto();
        saveReservedReqDto.setCafeId("64c45ac3935eb61c140793e7");
        saveReservedReqDto.setTableId("64a021f82ff24a6d8c7bd57c");

        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        String startTime = "2029-08-31 12:00:00";
        String endTime = "2029-08-31 14:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime newStartTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime newEndTime = LocalDateTime.parse(endTime, formatter);
        saveReservedReqDto.setStartTime(newStartTime);
        saveReservedReqDto.setEndTime(newEndTime);

        // when, then 겹친다면 CustomDBException을 내보낼 것이고, 아니라면 저장될 것이다.
        if (reservedRepository.isReservedByTableIdBetweenTime(saveReservedReqDto.getTableId(), newStartTime, newEndTime) ) {
            Assertions.assertThrows(CustomDBException.class, () -> reserveService.saveReserved(userId, saveReservedReqDto));
        } else {
            Assertions.assertDoesNotThrow(()-> reserveService.saveReserved(userId, saveReservedReqDto));
        }

    }



    @Test
    public void getMyReservedTest() {
        // given
        String userId = "test";

        List<Reserved> reserveds =  reservedRepository.findAll();
        for (Reserved reserved : reserveds) {
            System.out.println(reserved.getUser());
            System.out.println(reserved.getStartTime());
            System.out.println(reserved.getCafe().getId());
        }

        // then
        Assertions.assertDoesNotThrow(()-> reserveService.getMyReserved(userId, Pageable.ofSize(1)));

    }

    @Test
    public void getNearestReservedTest() {
        // given
        String userId = "test";
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String checkStartTime = "2029-08-10 12:00:00";

        Reserved reserved = Reserved.builder()
                .user(User.builder().id(userId).build())
                .cafe(Cafe.builder().id("cafe-id").name("cafe-name").address("cafe-address").image("cafe-image").build())
                .table(Table.builder().seating(1).build())
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .status(VALID)
                .build();

        reservedRepository.save(reserved);

        // when
        // then
        Assertions.assertDoesNotThrow(() -> reserveService.getNearestReserved(userId));


    }

    @BeforeAll
    public void deleteCafeAndTable() {
        tableRepository.deleteById("64a021f82ff24a6d8c7bd57c");
        cafeRepository.deleteById("64c45ac3935eb61c140793e7");
        userRepository.deleteById("test");
    }



}