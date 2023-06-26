package com.example.jariBean.repository;


import com.example.jariBean.entity.Cafe;
import com.example.jariBean.repository.cafe.CafeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CafeRepositoryTest {

    @Autowired
    CafeRepository cafeRepository;

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
    public void findByIdTest() {
        String id = "64954483a17c2758f6d41b4a";
        String cafeName = "(주)커피지아";
        String cafeAddress = "서울특별시 서초구 강남대로 27, AT센터 제1전시장 (양재동)";

        Cafe cafe = cafeRepository.findById(id).orElseThrow(() -> new IllegalStateException("해당 id와 일치하는 Cafe가 존재하지 않습니다."));

        assertThat(cafe.getCafeName()).isEqualTo(cafeName);
        assertThat(cafe.getCafeAddress()).isEqualTo(cafeAddress);
    }

    @Test
    public void findAllTest() throws Exception {
        List<Cafe> cafeList = cafeRepository.findAll();
        System.out.println("cafeList.size() = " + cafeList.size());
    }

}