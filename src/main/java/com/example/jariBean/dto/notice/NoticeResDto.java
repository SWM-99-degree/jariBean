package com.example.jariBean.dto.notice;

import com.example.jariBean.entity.Notice;

import java.time.LocalDateTime;

public class NoticeResDto {

    public static class NoticeSummaryResDto {
        private String title;
        private String content;
        private LocalDateTime createdAt;

        public NoticeSummaryResDto(Notice notice){
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.createdAt = notice.getCreatedAt();
        }
    }
}
