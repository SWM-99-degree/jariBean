package com.example.jariBean.service;

import com.example.jariBean.dto.notice.NoticeResDto.NoticeSummaryResDto;
import com.example.jariBean.entity.Notice;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<NoticeSummaryResDto> findNoticeList(Pageable pageable) {
        Page<Notice> noticeList = noticeRepository.findAllByOrderByCreatedAtDesc(pageable);
        Page<NoticeSummaryResDto> noticeSummaryResDtos = noticeList.map(notice -> new NoticeSummaryResDto(notice));
        return noticeSummaryResDtos;
    }
}
