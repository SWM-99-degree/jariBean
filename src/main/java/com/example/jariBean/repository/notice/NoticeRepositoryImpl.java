package com.example.jariBean.repository.notice;

import com.example.jariBean.entity.Cafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.geo.Metrics.KILOMETERS;


public class NoticeRepositoryImpl implements NoticeRepositoryTemplate {

    @Autowired private MongoTemplate mongoTemplate;

}
