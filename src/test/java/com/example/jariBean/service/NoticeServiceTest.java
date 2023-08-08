package com.example.jariBean.service;

import com.example.jariBean.handler.ex.CustomDBException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NoticeServiceTest {
    @Autowired
    private NoticeService noticeService;
    @Test
    public void noticeServiceTest(){
        //then
        Assertions.assertDoesNotThrow(()->noticeService.findNoticeList());

    }


}
