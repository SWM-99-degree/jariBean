package com.example.jariBean.dto.reserved;

import com.example.jariBean.dto.dbconnect.ReservedJoinTableDto;
import com.example.jariBean.entity.TableClass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.util.function.Tuple2;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReservedResDto {

    @Getter
    @Setter
    public static class NearestReservedResDto {
        private String cafeName;
        private String cafeImg;
        private Long leftTime;
        private LocalDateTime reservedStartTime;
        private List<TableClass.TableOption> tableOptions;

        @Builder
        public NearestReservedResDto(LocalDateTime time, ReservedJoinTableDto reservedJoinTableDto){
            this.cafeImg = reservedJoinTableDto.getCafeImg();
            this.reservedStartTime = reservedJoinTableDto.getReservedStartTime();
            this.leftTime = Duration.between(time, reservedJoinTableDto.getReservedStartTime()).toMinutes();
            this.cafeName = reservedJoinTableDto.getCafeName();
            this.tableOptions = reservedJoinTableDto.getTableOptions();
        }
    }

    @Getter
    @Setter
    public static class ReservedTableListResDto {
        private String cafeName;
        private String cafeImg;
        private LocalDateTime reservedStartTime;
        private List<TimeTable> timeTable;

        @Builder
        public ReservedTableListResDto() {
            this.timeTable = new ArrayList<>();
        }

        public void appendTimetable(TimeTable timeTable){
            this.timeTable.add(timeTable);
        }



        @Getter
        @Setter
        public static class TimeTable {
            private String tableId;
            private List<TableClass.TableOption> tableOptions;
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
