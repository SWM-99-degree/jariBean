package com.example.jariBean.repository;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.CafeOperatingTime;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.cafeOperatingTime.CafeOperatingTimeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class CafeOperatingRepositoryTest {
    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private CafeOperatingTimeRepository cafeOperatingTimeRepository;

    @Test
    public void test4() {
        Cafe cafe = cafeRepository.findByCafePhoneNumber("01012341234").orElseThrow();
        CafeOperatingTime cafeOperatingTime = new CafeOperatingTime();
        cafeOperatingTime.setCafeId(cafe.getId());
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 0, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 12, 0);

        cafeOperatingTime.setOpenTime(dateTime1);
        cafeOperatingTime.setCloseTime(dateTime2);
        cafeOperatingTimeRepository.save(cafeOperatingTime);
    }
}
