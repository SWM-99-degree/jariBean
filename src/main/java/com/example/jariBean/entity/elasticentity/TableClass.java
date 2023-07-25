package com.example.jariBean.entity.elasticentity;

import com.example.jariBean.entity.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cafe.tableClass")
public class TableClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String cafeId;

    private String name;

    private List<TableOption> tableOptionList;
    // 생성일자(createdDate)에 대한 정보는 생성시에만 할당 가능, 갱신 불가
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Integer version;

    public enum TableOption {
        PLUG, HEIGHT, RECTANGLE, BACKREST
    }

}
