package com.example.jariBean.service;

import com.example.jariBean.entity.elasticentity.Cafe;
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
        System.out.println(cafes);
        cafes.forEach(cafe -> System.out.println(cafe.getId()));

        System.out.println(LocalDateTime.now());
    }
}
