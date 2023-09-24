package com.example.jariBean.repository.table;

import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalTime;
import java.util.List;

public class TableRepositoryImpl implements TableRepositoryTemplate{
    @Autowired private MongoTemplate mongoTemplate;


    @Override
    public List<Table> findByConditions(String cafeId, Integer seating, List<TableClass.TableOption> tableOptionList) {
        Criteria criteria = Criteria.where("cafeId").is(cafeId);

        if (tableOptionList != null && !tableOptionList.isEmpty()) {
            criteria.and("tableOptionList").all(tableOptionList);
        }
        if (seating != null) {
            criteria.and("seating").gte(seating);
        }

        return mongoTemplate.find(new Query(criteria), Table.class);
    }
}
