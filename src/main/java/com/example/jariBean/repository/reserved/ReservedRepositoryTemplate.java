package com.example.jariBean.repository.reserved;

import com.example.jariBean.dto.dbconnect.ReservedJoinTableDto;
import com.example.jariBean.entity.Reserved;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservedRepositoryTemplate {
    Reserved findNearestReserved(String userId, LocalDateTime time);

    List<Reserved> findReservedByIdBetweenTime(String cafeId, LocalDateTime time);

    List<Reserved> findReservedByIdAndTableIdBetweenTime(String cafeId, String tableId,LocalDateTime time);


    boolean isReservedByTableIdBetweenTime(String tableId, LocalDateTime startTime, LocalDateTime endTime);

}
