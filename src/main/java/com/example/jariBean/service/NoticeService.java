package com.example.jariBean.service;

import com.example.jariBean.dto.notice.NoticeResDto.NoticeSummaryResDto;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private NoticeRepository noticeRepository;

    public List<NoticeSummaryResDto> findNoticeList() {
        List<NoticeSummaryResDto> noticeList = new ArrayList<>();
        try {
            noticeRepository.findAllByOrderByCreatedAtDesc().forEach(notice -> noticeList.add(new NoticeSummaryResDto(notice)));
            return noticeList;
        } catch (Exception e) {
            throw new CustomDBException("공지 DB에 오류가 있습니다.");
        }
    }
}
