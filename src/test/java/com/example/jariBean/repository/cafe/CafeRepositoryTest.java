package com.example.jariBean.repository.cafe;


import com.example.jariBean.entity.Cafe;
import com.example.jariBean.handler.ex.CustomApiException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CafeRepositoryTest {

    @Autowired CafeRepository cafeRepository;

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
    public void findByIdTest() throws Exception {
        // given

        // when
        Cafe findCafe = cafeRepository.findById(id).orElseThrow(() -> new CustomApiException("id에 해당하는 cafe가 존재하지 않습니다."));

        // then
        assertThat(findCafe.getCafeName()).isEqualTo(cafeName);
        assertThat(findCafe.getCafeAddress()).isEqualTo(cafeAddress);
    }
    
    @Test
    public void findByCafeNameTest() throws Exception {
        // given
        
        // when
        Cafe findCafe = cafeRepository.findByCafeName(cafeName).orElseThrow(() -> new CustomApiException("cafeName에 해당하는 cafe가 존재하지 않습니다."));

        // then
        assertThat(findCafe.getId()).isEqualTo(id);
        assertThat(findCafe.getCafeAddress()).isEqualTo(cafeAddress);
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

}