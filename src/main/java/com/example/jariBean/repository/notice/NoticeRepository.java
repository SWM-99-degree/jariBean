package com.example.jariBean.repository.notice;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends MongoRepository<Notice, String>, NoticeRepositoryTemplate {

    Page<Notice> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
