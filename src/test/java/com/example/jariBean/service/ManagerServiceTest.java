package com.example.jariBean.service;

import com.example.jariBean.dto.manager.ManagerResDto.ManagerReserveResDto;
import com.example.jariBean.dto.manager.ManagerResDto.ManagerTableResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.entity.TableClass.TableOption;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.tableClass.TableClassRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.jariBean.entity.TableClass.TableOption.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ManagerServiceTest {

    @Autowired private CafeRepository cafeRepository;
    @Autowired private TableClassRepository tableClassRepository;
    @Autowired private TableRepository tableRepository;
    @Autowired private ReservedRepository reservedRepository;
    @Autowired private ManagerService managerService;

    private Cafe globalCafe;

    @BeforeEach
    public void beforeEach() {

        Cafe cafe = Cafe.builder().build();
        globalCafe = cafeRepository.save(cafe);

        TableClass tc1 = TableClass.builder().cafeId(globalCafe.getId()).name("tc1").build();
        TableClass tc2 = TableClass.builder().cafeId(globalCafe.getId()).name("tc2").build();
        TableClass tc3 = TableClass.builder().cafeId(globalCafe.getId()).name("tc3").build();

        TableClass savedTc1 = tableClassRepository.save(tc1);
        TableClass savedTc2 = tableClassRepository.save(tc2);
        TableClass savedTc3 = tableClassRepository.save(tc3);

        Table t1 = Table.builder().cafeId(globalCafe.getId()).tableClassId(savedTc1.getId()).name("t1").build();
        Table t2 = Table.builder().cafeId(globalCafe.getId()).tableClassId(savedTc1.getId()).name("t2").build();
        Table t3 = Table.builder().cafeId(globalCafe.getId()).tableClassId(savedTc1.getId()).name("t3").build();

        tableRepository.save(t1);
        tableRepository.save(t2);
        tableRepository.save(t3);

        Table t4 = Table.builder().cafeId(globalCafe.getId()).tableClassId(savedTc2.getId()).name("t4").build();
        Table t5 = Table.builder().cafeId(globalCafe.getId()).tableClassId(savedTc2.getId()).name("t5").build();
        Table t6 = Table.builder().cafeId(globalCafe.getId()).tableClassId(savedTc2.getId()).name("t6").build();

        tableRepository.save(t4);
        tableRepository.save(t5);
        tableRepository.save(t6);

        Table t7 = Table.builder().cafeId(globalCafe.getId()).tableClassId(savedTc3.getId()).name("t7").build();
        Table t8 = Table.builder().cafeId(globalCafe.getId()).tableClassId(savedTc3.getId()).name("t8").build();
        Table t9 = Table.builder().cafeId(globalCafe.getId()).tableClassId(savedTc3.getId()).name("t9").build();

        tableRepository.save(t7);
        tableRepository.save(t8);
        tableRepository.save(t9);

        LocalDateTime now = LocalDateTime.now();

        Reserved r1 = Reserved.builder().table(t1).cafe(cafe).startTime(now).endTime(now.plusMinutes(120)).userId("user-1").build();
        Reserved r2 = Reserved.builder().table(t1).cafe(cafe).startTime(now).endTime(now.plusMinutes(120)).userId("user-2").build();
        Reserved r3 = Reserved.builder().table(t1).cafe(cafe).startTime(now).endTime(now.plusMinutes(120)).userId("user-2").build();
        Reserved r4 = Reserved.builder().table(t2).cafe(cafe).startTime(now).endTime(now.plusMinutes(120)).userId("user-1").build();
        Reserved r5 = Reserved.builder().table(t2).cafe(cafe).startTime(now).endTime(now.plusMinutes(120)).userId("user-3").build();
        Reserved r6 = Reserved.builder().table(t3).cafe(cafe).startTime(now).endTime(now.plusMinutes(120)).userId("user-4").build();

        reservedRepository.save(r1);
        reservedRepository.save(r2);
        reservedRepository.save(r3);
        reservedRepository.save(r4);
        reservedRepository.save(r5);
        reservedRepository.save(r6);


    }

    @Test
    public void toggleMatchingStatusTest() throws Exception {
        // given
        Cafe cafe = Cafe.builder().build();
        boolean beforeStatus = cafe.isMatching();

        // when
        boolean afterStatus = cafe.toggleMatchingStatus();

        // then
        assertThat(beforeStatus).isNotEqualTo(afterStatus);
    }

    @Test
    public void getTablePageTest() throws Exception {
        // given
        String cafeId = globalCafe.getId();

        // when
        List<ManagerTableResDto> tablePage = managerService.getTablePage(cafeId);

        // then
        tablePage.forEach(managerTableResDto -> {
            System.out.println("managerTableResDto.getName() = " + managerTableResDto.getName());
            managerTableResDto.getTableSummaryDtoList().forEach(tableSummaryDto -> {
                System.out.println("tableSummaryDto.getName() = " + tableSummaryDto.getName());
            });
        });

        Assertions.assertThat(tablePage.size()).isEqualTo(3);
        for (ManagerTableResDto managerTableResDto : tablePage) {
            Assertions.assertThat(managerTableResDto.getTableSummaryDtoList().size()).isEqualTo(3);
        }
    }

    @Test
    public void getReservePageTest() throws Exception {
        // given
        String cafeId = globalCafe.getId();

        // when
        ManagerReserveResDto managerReserveResDto = managerService.getReservePage(cafeId);

        // then
        managerReserveResDto.getTableClassSummaryDtoList().forEach(tableClassSummaryDto -> {
            System.out.println("tableClassSummaryDto.getName() = " + tableClassSummaryDto.getName());
        });

        managerReserveResDto.getTableReserveDtoList().forEach(tableReserveDto -> {
            System.out.println("tableReserveDto.getTableName() = " + tableReserveDto.getTableName());
            tableReserveDto.getReservePeriodDtoList().forEach(reservePeriodDto -> {
                System.out.println("reservePeriodDto.getUsername() = " + reservePeriodDto.getUsername());
                System.out.println("reservePeriodDto.getStartTime() = " + reservePeriodDto.getStartTime());
                System.out.println("reservePeriodDto.getEndTime() = " + reservePeriodDto.getEndTime());
            });
        });

        Assertions.assertThat(managerReserveResDto.getTableClassSummaryDtoList().size()).isEqualTo(3);
        Assertions.assertThat(managerReserveResDto.getTableReserveDtoList().size()).isEqualTo(9);

    }

    @Test
    public void updateTableTest() throws Exception {
        // given
        Table table = Table.builder().build();
        String name = "table-name";
        String description = "table-description";
        String image = "table-image";

        // when
        table.update(name,description,image);
        Table savedTable = tableRepository.save(table);

        // then
        Assertions.assertThat(savedTable.getName()).isEqualTo(name);
        Assertions.assertThat(savedTable.getDescription()).isEqualTo(description);
        Assertions.assertThat(savedTable.getImage()).isEqualTo(image);
    }

    @Test
    public void addTableTest() throws Exception {
        // given
        Table table = Table.builder().build();
        String name = "table-name";
        String description = "table-description";
        String image = "table-image";

        // when
        table.update(name,description,image);
        Table savedTable = tableRepository.save(table);

        // then
        Assertions.assertThat(savedTable.getName()).isEqualTo(name);
        Assertions.assertThat(savedTable.getDescription()).isEqualTo(description);
        Assertions.assertThat(savedTable.getImage()).isEqualTo(image);
    }

    @Test
    public void updateTableClassTest() throws Exception {
        // given
        TableClass tableClass = TableClass.builder()
                .name("before-name")
                .seating(1)
                .tableOptionList(List.of(HEIGHT, RECTANGLE, PLUG))
                .build();

        // when
        String afterName = "after-name";
        Integer afterSeating = 2;
        List<TableOption> afterOption = List.of(PLUG);
        tableClass.update(afterName, afterSeating, afterOption);

        TableClass savedTableClass = tableClassRepository.save(tableClass);

        // then
        Assertions.assertThat(savedTableClass.getName()).isEqualTo(afterName);
        Assertions.assertThat(savedTableClass.getSeating()).isEqualTo(afterSeating);
        Assertions.assertThat(savedTableClass.getTableOptionList()).isEqualTo(afterOption);
    }

    @Test
    public void addTableClassTest() throws Exception {
        // given
        String cafeId = "cafe-id";
        String name = "table-class-name";
        Integer seating = 2;
        List<TableOption> option = List.of(PLUG);

        // when
        TableClass tableClass = TableClass.builder()
                .cafeId(cafeId)
                .build();
        tableClass.update(name, seating, option);
        TableClass savedTableClass = tableClassRepository.save(tableClass);

        // then
        Assertions.assertThat(savedTableClass.getCafeId()).isEqualTo(cafeId);
        Assertions.assertThat(savedTableClass.getName()).isEqualTo(name);
        Assertions.assertThat(savedTableClass.getSeating()).isEqualTo(seating);
        Assertions.assertThat(savedTableClass.getTableOptionList()).isEqualTo(option);

    }

}