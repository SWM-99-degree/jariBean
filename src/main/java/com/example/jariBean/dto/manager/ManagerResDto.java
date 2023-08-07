package com.example.jariBean.dto.manager;

import com.example.jariBean.entity.TableClass.TableOption;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class ManagerResDto {

    @Data
    public static class ManagerReserveResDto {
        private List<TableClassSummaryDto> tableClassSummaryDtoList;
        private List<TableReserveDto> tableReserveDtoList;
    }

    @Data
    public static class ManagerTableResDto {
        private List<TableClassSummaryDto> tableClassSummaryDtoList;
    }


    @Data
    public static class TableClassSummaryDto {
        private String id;
        private String description;
        private Integer seating;
        private List<TableOption> option;
        private List<TableSummaryDto> tableSummaryDtoList;
    }

    @Data
    public static class TableSummaryDto {
        private String id;
        private String name;
        private String description;
        private String image;
    }

    @Data
    public static class TableReserveDto {
        private String tableId;
        private String tableName;
        private List<ReservePeriodDto> reservePeriodDtoList;
    }

    @Data
    public static class ReservePeriodDto {
        private String username;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }



}
