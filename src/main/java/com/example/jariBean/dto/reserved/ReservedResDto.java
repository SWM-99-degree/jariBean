package com.example.jariBean.dto.reserved;

import com.example.jariBean.dto.dbconnect.ReservedJoinTableDto;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.TableClass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReservedResDto {

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
        private List<TableClass.TableOption> tableOptions;

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
