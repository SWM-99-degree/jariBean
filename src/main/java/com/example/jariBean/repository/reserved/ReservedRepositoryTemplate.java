package com.example.jariBean.repository.reserved;

import com.example.jariBean.dto.dbconnect.ReservedJoinTableDto;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.TableClass;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservedRepositoryTemplate {

    List<String> findCafeByReserved(List<String> cafes, LocalDateTime startTime, LocalDateTime endTime, List<TableClass.TableOption> tableOptionList);
    Reserved findNearestReserved(String userId, LocalDateTime time);

    List<Reserved> findReservedByIdBetweenTime(String cafeId, LocalDateTime startTime, LocalDateTime endTime);

    List<Reserved> findReservedByIdAndTableIdBetweenTime(String cafeId, String tableId,LocalDateTime time);

    List<Reserved> findTodayReservedById(String cafeId);

    boolean isReservedByTableIdBetweenTime(String tableId, LocalDateTime startTime, LocalDateTime endTime);

}
