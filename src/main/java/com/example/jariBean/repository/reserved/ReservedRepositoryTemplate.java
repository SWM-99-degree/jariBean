package com.example.jariBean.repository.reserved;

import com.example.jariBean.dto.dbconnect.ReservedJoinTableDto;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.TableClass;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservedRepositoryTemplate {

    List<Reserved> findReservedByConditions(String userId, LocalDateTime startTime, Integer seating, List<TableClass.TableOption> tableOptionList);

    List<String> findCafeByReserved(List<String> cafes, LocalDateTime startTime, LocalDateTime endTime, Integer seating, List<TableClass.TableOption> tableOptionList);
    Reserved findNearestReserved(String userId, LocalDateTime time);

    List<Reserved> findReservedByIdBetweenTime(String cafeId, LocalDateTime startTime, LocalDateTime endTime);

    List<Reserved> findTodayReservedById(String cafeId, LocalDateTime time);

    boolean isReservedByTableIdBetweenTime(String tableId, LocalDateTime startTime, LocalDateTime endTime);

}
