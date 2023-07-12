package com.example.jariBean.repository;


import com.example.jariBean.entity.Cafe;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.cafeOperatingTime.CafeOperatingTimeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.tableClass.TableClassRepository;
import com.example.jariBean.repository.user.UserRepository;
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