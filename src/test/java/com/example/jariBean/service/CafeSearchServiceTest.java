package com.example.jariBean.service;

import com.example.jariBean.entity.Cafe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CafeSearchServiceTest {

    @Autowired
    SearchService searchService;


    @Test
    public void findByTextwithELKTest(){
        System.out.println(LocalDateTime.now());
        List<com.example.jariBean.entity.Cafe> cafes = new ArrayList<>();
        //cafes = searchService.findByText("*test*");
        //System.out.println(cafes);
        cafes.forEach(cafe -> System.out.println(cafe.getId()));
        System.out.println(LocalDateTime.now());
    }

    @Test
    public void findBySearchingTest(){

        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 15, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 16, 0);
        List<Cafe> cafes = searchService.findByText("미추홀",  37.4467039276238, 37.4467039276238, dateTime1, dateTime2, null);
        for (Cafe cafe : cafes) {
            System.out.println(cafe.getId());
        }
    }


}
