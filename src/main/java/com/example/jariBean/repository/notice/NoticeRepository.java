package com.example.jariBean.repository.notice;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Notice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends MongoRepository<Notice, String>, NoticeRepositoryTemplate {

    List<Notice> findAllByOrderByCreatedAtDesc();

}
