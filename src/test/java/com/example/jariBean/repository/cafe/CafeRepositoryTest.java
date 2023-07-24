package com.example.jariBean.repository.cafe;


import com.example.jariBean.dto.reserved.ReservedReqDto;
import com.example.jariBean.dto.reserved.ReservedResDto;
import com.example.jariBean.entity.*;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.cafeOperatingTime.CafeOperatingTimeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.tableClass.TableClassRepository;
import com.example.jariBean.repository.user.UserRepository;
import com.example.jariBean.service.ReserveService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CafeRepositoryTest {

    @Autowired CafeRepository cafeRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TableRepository tableRepository;
    @Autowired
    CafeOperatingTimeRepository cafeOperatingTimeRepository;
    @Autowired
    ReservedRepository reservedRepository;
    @Autowired
    TableClassRepository tableClassRepository;

    private final String id = "64954483a17c2758f6d41b4a";
    private final String cafeName = "(주)커피지아";
    private final String cafeAddress = "서울특별시 서초구 강남대로 27, AT센터 제1전시장 (양재동)";

    @Test
    public void saveAndDeleteTest() throws Exception {
        // given
        String cafeName = "testCafeName";
        String cafePhoneNumber = "12345678910";
        String cafePassword = "1234567890";
        String cafeAddress = "testCafeAddress";

        Cafe cafe = Cafe.builder()
                .cafeName(cafeName)
                .cafePhoneNumber(cafePhoneNumber)
                .cafePassword(cafePassword)
                .cafeAddress(cafeAddress)
                .build();
        // when
        Cafe savedCafe = cafeRepository.save(cafe);

        //then
        assertThat(savedCafe.getCafeName()).isEqualTo(cafeName);
        assertThat(savedCafe.getCafePhoneNumber()).isEqualTo(cafePhoneNumber);
        assertThat(savedCafe.getCafePassword()).isEqualTo(cafePassword);
        assertThat(savedCafe.getCafeAddress()).isEqualTo(cafeAddress);

        // delete
        cafeRepository.deleteById(savedCafe.getId());
    }

    @Test
    public void saveAllAndDeleteAllTest() throws Exception {
        // given
        String cafeName = "testCafeName";
        String cafePhoneNumber = "12345678910";
        String cafePassword = "1234567890";
        String cafeAddress = "testCafeAddress";

        List<Cafe> cafeList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Cafe newCafe = Cafe.builder()
                    .cafeName(cafeName)
                    .cafePhoneNumber(cafePhoneNumber)
                    .cafePassword(cafePassword)
                    .cafeAddress(cafeAddress)
                    .build();
            cafeList.add(newCafe);
        }

        // when
        List<Cafe> savedCafeList = cafeRepository.saveAll(cafeList);

        // delete
        List<String> savedCafeIdList = savedCafeList.stream().map(Cafe::getId).collect(Collectors.toList());
        cafeRepository.deleteAllById(savedCafeIdList);
    }



    @Test
    public void test2() {
        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
        Cafe cafe = cafeRepository.findByCafePhoneNumber("01012341234").orElseThrow();
        LocalDateTime dateTime3 = LocalDateTime.of(2023, 7, 1, 18-9, 0);
        LocalDateTime dateTime4 = LocalDateTime.of(2023, 7, 1, 19-9, 0);
        Table table = tableRepository.findById("64a27eaf7244d72ba3c28d1b").orElseThrow();
        Reserved reserved = new Reserved(user.getId(), cafe.getId(), table.getId(),dateTime3, dateTime4);

        reservedRepository.save(reserved);


    }

    @Test
    public void test3(){
        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
        Cafe cafe = cafeRepository.findByCafePhoneNumber("01012341234").orElseThrow();
        Table table = tableRepository.findById("64a021f82ff24a6d8c7bd57c").orElseThrow();
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 15-9, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 17-9, 0);

        LocalDateTime dateTime3 = LocalDateTime.of(2023, 7, 1, 18-9, 0);
        LocalDateTime dateTime4 = LocalDateTime.of(2023, 7, 1, 19-9, 0);

        Reserved reserved1 = new Reserved(user.getId(),cafe.getId(),table.getId(), dateTime1, dateTime2);
        Reserved reserved2 = new Reserved(user.getId(),cafe.getId(),table.getId(), dateTime3, dateTime4);
        reservedRepository.save(reserved1);
        reservedRepository.save(reserved2);


        List<Reserved> reserveds =new ArrayList<>();
        reserveds.add(reserved1);
        reserveds.add(reserved2);

        cafe.setReservedList(reserveds);
        cafeRepository.save(cafe);


    }

    @Test
    public void test4() {
        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
        Cafe cafe = cafeRepository.findByCafePhoneNumber("01012341234").orElseThrow();
        CafeOperatingTime cafeOperatingTime = new CafeOperatingTime();
        cafeOperatingTime.setCafeId(cafe.getId());
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 0, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 12, 0);

        cafeOperatingTime.setOpenTime(dateTime1);
        cafeOperatingTime.setCloseTime(dateTime2);
        cafeOperatingTimeRepository.save(cafeOperatingTime);
    }
    @Test
    public void method2() {
        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
        Cafe cafe = cafeRepository.findByCafePhoneNumber("01012341234").orElseThrow();
        ReserveService reserveService = new ReserveService(reservedRepository, cafeRepository);
        ReservedReqDto.SaveReservedReqDto saveReservedReqDto = new ReservedReqDto.SaveReservedReqDto();
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 8, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 9, 0);
        // System.out.println(reserveService.findReservedListByCafeId(cafe.getId(), dateTime1));
    }

    @Test
    public void method3() {
        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
        Cafe cafe = cafeRepository.findByIdwithOperatingTime("64884c1d65989d25539387b5");
        System.out.println(cafe.getCafeOperatingTimeList().get(0).getOpenTime());
        ReserveService reserveService = new ReserveService(reservedRepository, cafeRepository);

        ReservedReqDto.NearestReservedReqDto nearestReservedReqDto = new ReservedReqDto.NearestReservedReqDto();
        nearestReservedReqDto.setUserId("64884c1d65989d25539387b5");
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 7, 0);
        nearestReservedReqDto.setUserNow(dateTime1);
        ReservedResDto.NearestReservedResDto nearestReservedResDto = reserveService.getNearestReserved(nearestReservedReqDto);
        System.out.println(nearestReservedResDto);
    }

    @Test
    public void method4() {
        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
        Cafe cafe = cafeRepository.findByCafePhoneNumber("01012341234").orElseThrow();
        ReserveService reserveService = new ReserveService(reservedRepository, cafeRepository);
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 7, 0);
        ReservedResDto.ReservedTableListResDto reservedTableListResDto = new ReservedResDto.ReservedTableListResDto();
        //reservedTableListResDto = reserveService.findReservedListByCafeId(cafe.getId(), dateTime1);

        for (ReservedResDto.ReservedTableListResDto.TimeTable timeTable : reservedTableListResDto.getTimeTables()){
            System.out.println("newTable");
            for (ReservedResDto.ReservedTableListResDto.TimeTable.ReservingTime reservingTime : timeTable.getReservingTimes()){
                System.out.println("-----");
                System.out.println(reservingTime.getStartTime());
                System.out.println(reservingTime.getEndTime());
            }
        }
        System.out.println(reservedTableListResDto.getTimeTables());

    }

    @Test
    public void method() {
        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
        Cafe cafe = cafeRepository.findByCafePhoneNumber("01012341234").orElseThrow();
        ReserveService reserveService = new ReserveService(reservedRepository, cafeRepository);
        ReservedReqDto.SaveReservedReqDto saveReservedReqDto = new ReservedReqDto.SaveReservedReqDto();
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 8, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 9, 0);
        saveReservedReqDto.setReservedStartTime(dateTime1);
        saveReservedReqDto.setReservedEndTime(dateTime2);
        saveReservedReqDto.setTableId("64a021f82ff24a6d8c7bd57c");
        saveReservedReqDto.setUserId(user.getId());
        saveReservedReqDto.setCafeId(cafe.getId());

        reserveService.saveReserved(saveReservedReqDto);
    }

    @Test
    public void updateCafeNameTest() throws Exception {
        // given
        String cafeName = "testCafeName";
        String newCafeName = "testNewCafeName";
        String cafePhoneNumber = "12345678910";
        String cafePassword = "1234567890";
        String cafeAddress = "testCafeAddress";

        Cafe cafe = Cafe.builder()
                .cafeName(cafeName)
                .cafePhoneNumber(cafePhoneNumber)
                .cafePassword(cafePassword)
                .cafeAddress(cafeAddress)
                .build();

        Cafe savedCafe = cafeRepository.save(cafe);

        // when
        savedCafe.updateCafeName(newCafeName);
        cafeRepository.save(savedCafe);

        // then
        assertThat(savedCafe.getCafeName()).isEqualTo(newCafeName);

        // delete
        cafeRepository.deleteById(savedCafe.getId());
    }

    @Test
    public void findByCoordinateNearTest() throws Exception {
        GeoJsonPoint coordinate = new GeoJsonPoint(126.9841888305794, 37.53697168039137);
        Double distance = 50D;

        List<Cafe> cafeList = cafeRepository.findByCoordinateNear(coordinate, distance);
        System.out.println("byCoordinateDistance.getContent().size() = " + cafeList.size());
        cafeList.forEach(cafe -> {
            System.out.println(cafe.getCafeName());
        });
    }


    @Test
    public void findAllTest() throws Exception {
        List<Cafe> cafeList = cafeRepository.findAll();
        System.out.println("cafeList.size() = " + cafeList.size());
    }


}