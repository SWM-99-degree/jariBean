package com.example.jariBean.repository.cafe;


import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
class CafeRepositoryTest {

    @Autowired private CafeRepository cafeRepository;

    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private ReservedRepository reservedRepository;

    private final String cafeName = "(주)커피지아";
    private final String cafeAddress = "서울특별시 서초구 강남대로 27, AT센터 제1전시장 (양재동)";


    @BeforeAll
    public void saveCafesTable() {

        List<String> cafes = new ArrayList<>();
        cafes.add("64c45ac3935eb61c140793e8");
        for (String cafe : cafes) {
            List<TableClass.TableOption> options = new ArrayList<>();
            options.add(TableClass.TableOption.PLUG);
            options.add(TableClass.TableOption.RECTANGLE);
            Table table = new Table();
            table.setCafeId(cafe);
            table.setTableOptionList(options);
            tableRepository.save(table);
        }

    }


    @Test
    public void saveCafesReserved() {
        List<String> cafes = new ArrayList<>();
        cafes.add("64c45ac3935eb61c140793e8");
        for (String cafe : cafes) {
            Table table = tableRepository.findByCafeId(cafe).get(0);
            LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 15, 0);
            LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 17, 0);

            Reserved reserved = new Reserved("123", new Cafe(), table, dateTime1, dateTime2);
            reservedRepository.save(reserved);
        }

    }



    @Test
    public void saveAndDeleteTest() throws Exception {
        // given
        String cafeName = "testCafeName";
        String cafePhoneNumber = "12345678910";
        String cafePassword = "1234567890";
        String cafeAddress = "testCafeAddress";
        GeoJsonPoint jsonPoint = new GeoJsonPoint(12.123123,13.131313);
        Cafe cafe = Cafe.builder()
                .name(cafeName)
                .phoneNumber(cafePhoneNumber)
                .description(" this is so beautiful caffee")
                .address(cafeAddress)
                .coordinate(jsonPoint)
                .build();
        // when
        Cafe savedCafe = cafeRepository.save(cafe);

        //then
        Assertions.assertEquals(savedCafe.getName(),cafeName);
        Assertions.assertEquals(savedCafe.getAddress(),cafeAddress);
        Assertions.assertEquals(savedCafe.getPhoneNumber(),cafePhoneNumber);

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
        GeoJsonPoint coordinate = new GeoJsonPoint(126.9841888305794, 37.53697168039137);



        for (int i = 0; i < 10; i++) {
            Cafe newCafe = Cafe.builder()
                    .name(cafeName)
                    .coordinate(coordinate)
                    .phoneNumber(cafePhoneNumber)
                    .address(cafeAddress)
                    .build();
            cafeList.add(newCafe);
        }

        // when
        List<Cafe> savedCafeList = cafeRepository.saveAll(cafeList);

    }



//    @Test
//    public void findByCoordinateNearTest() throws Exception {
//        String word = "무인";
//        List<String> words = new ArrayList<>();
//        words.add(word);
//
//        GeoJsonPoint coordinate = new GeoJsonPoint(126.9841888305794, 37.53697168039137);
//
//        List<String> cafeList = cafeRepository.findByWordAndCoordinateNear(words,coordinate);
//        System.out.println("byCoordinateDistance.getContent().size() = " + cafeList.size());
//        cafeList.forEach(cafe -> {
//            System.out.println(cafe);
//        });
//    }


    @Test
    public void findAllTest() throws Exception {
        List<Cafe> cafeList = cafeRepository.findAll();
        System.out.println("cafeList.size() = " + cafeList.size());
    }


}