package com.example.jariBean.dto.reserved;

import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import com.example.jariBean.dto.table.TableResDto.TableDetailDto;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.TableClass.TableOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReservedResDto {


    @Getter
    @Setter
    public static class ReserveSummaryResDto {
        private String reserveId;
        private LocalDateTime reserveStartTime;
        private Integer matchingSeating;
        private CafeSummaryDto cafeSummaryDto;

        public ReserveSummaryResDto(Reserved reserved){
            this.reserveId = reserved.getId();
            this.reserveStartTime = reserved.getReservedStartTime();
            this.matchingSeating = reserved.getTable().getSeating();
            this.cafeSummaryDto = new CafeSummaryDto(reserved.getCafe());
        }
    }

    @Getter
    @Setter
    public static class availableTime {
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public availableTime(LocalDateTime startTime, LocalDateTime endTime){
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    @Getter
    @Setter
    public static class TableReserveResDto {
        private TableDetailDto tableDetailDto;
        private List<availableTime> availableTimeList;

        public void addTime(availableTime availableTime) {
            this.availableTimeList.add(availableTime);
        }
    }

    @Getter
    @Setter
    public static class NearestReservedResDto {
        private String cafeId;
        private String cafeName;
        private String cafeImg;
        private String cafeAddress;
        private LocalDateTime reservedStartTime;
        private LocalDateTime reservedEndTime;
        // test
        private List<TableOption> tableOptions;

        @Builder
        public NearestReservedResDto(LocalDateTime time, Reserved reserved){
            this.cafeId = reserved.getCafeId();
            this.cafeImg = reserved.getCafe().getCafeImg();
            this.cafeAddress = reserved.getCafe().getAddress();
            this.reservedStartTime = reserved.getReservedStartTime();
            this.reservedEndTime = reserved.getReservedEndTime();
            this.cafeName = reserved.getCafe().getName();
        }
    }

    @Getter
    @Setter
    public static class ReservedTableListResDto {
        private String cafeName;
        private String cafeImg;
        private List<TimeTable> timeTables;

        @Builder
        public ReservedTableListResDto() {
            this.timeTables = new ArrayList<>();
        }

        public void appendTimetable(TimeTable timeTable){
            this.timeTables.add(timeTable);
        }



        @Getter
        @Setter
        public static class TimeTable {
            private String tableId;
            private List<TableOption> tableOptions;
            private List<ReservingTime> reservingTimes;

            @Builder
            public void TimeTable(){
                this.reservingTimes = new ArrayList<>();
            }

            public void appendReservingTime(ReservingTime reservingTime){
                this.reservingTimes.add(reservingTime);
            }


            @Getter
            @Setter
            public static class ReservingTime {
                private LocalDateTime startTime;
                private LocalDateTime endTime;

                public ReservingTime(LocalDateTime startTime, LocalDateTime endTime){
                    this.startTime = startTime;
                    this.endTime = endTime;
                }
            }
        }
    }
}
