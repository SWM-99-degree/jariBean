package com.example.jariBean.service;

import com.example.jariBean.dto.notice.NoticeResDto;
import com.example.jariBean.entity.Notice;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.notice.NoticeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
public class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private NoticeRepository noticeRepository;
    @Test
    public void noticeServiceTest(){
        //given
        Notice notice = new Notice("제목", "내용");
        noticeRepository.save(notice);

        // when
        NoticeResDto.NoticeSummaryResDto noticeSummaryResDtos = noticeService.findNoticeList(Pageable.unpaged()).get(0);

        //then
        Assertions.assertEquals(noticeSummaryResDtos.getTitle(), "제목");
        Assertions.assertEquals(noticeSummaryResDtos.getContent(), "내용");

    }


}
