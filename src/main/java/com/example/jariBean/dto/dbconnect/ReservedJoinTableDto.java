package com.example.jariBean.dto.dbconnect;

import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.TableClass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReservedJoinTableDto {

    private String reservedId;

    private String userId;

    private String cafeId;

    private String tableId;

    private LocalDateTime reservedStartTime;

    private LocalDateTime reservedEndTime;

    private Reserved.ReservedStatus reservedStatus;

    private String cafeName;

    private String cafeImg;

    @Field
    private List<TableClass.TableOption> tableOptions;



}
