package com.example.jariBean.entity.elasticentity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cafe.table")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String cafeId;

    private String tableClassId;

    private String description;

    private Integer seating; // 인원 수

    private String imageUrl;

    private String number;

    private List<TableClass.TableOption> tableOptionList;

}
