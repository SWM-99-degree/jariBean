package com.example.jariBean.service;

import com.example.jariBean.dto.manager.ManagerReqDto.ManagerLoginReqDto;
import com.example.jariBean.dto.manager.ManagerReqDto.ManagerTableClassReqDto;
import com.example.jariBean.dto.manager.ManagerResDto.ManagerLoginResDto;
import com.example.jariBean.dto.manager.ManagerResDto.ManagerReserveResDto;
import com.example.jariBean.dto.manager.ManagerResDto.ManagerTableResDto;
import com.example.jariBean.dto.manager.ManagerResDto.TableReserveDto;
import com.example.jariBean.entity.CafeManager;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.cafemanager.CafeManagerRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.tableClass.TableClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.jariBean.dto.manager.ManagerReqDto.ManagerTableReqDto;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final CafeRepository cafeRepository;
    private final TableRepository tableRepository;
    private final TableClassRepository tableClassRepository;
    private final ReservedRepository reservedRepository;

    private final PasswordEncoder passwordEncoder;
    private final CafeManagerRepository cafeManagerRepository;

    public void toggleMatchingStatus(String id) {
        cafeRepository.findById(id)
                .orElseThrow(() -> new CustomDBException("id에 해당하는 Cafe가 존재하지 않습니다."))
                        .toggleMatchingStatus();
    }

    public List<ManagerTableResDto> getTablePage(String cafeId) {

        List<TableClass> tableClassList = tableClassRepository.findByCafeId(cafeId);
        List<Table> tableList = tableRepository.findByCafeId(cafeId);

        return tableClassList.stream()
                .map(tableClass -> {
                    ManagerTableResDto managerTableResDto = new ManagerTableResDto();
                    managerTableResDto.addTableClassSummaryDto(tableClass);
                    tableList.stream()
                            .filter(table -> table.getTableClassId().equals(tableClass.getId()))
                            .collect(Collectors.toList())
                            .forEach(table -> {
                                managerTableResDto.addTableSummaryDto(table);
                            });
                    return managerTableResDto;
                }).collect(Collectors.toList());
    }

    public ManagerReserveResDto getReservePage(String cafeId) {

        ManagerReserveResDto managerReserveResDto = new ManagerReserveResDto();

        List<TableClass> tableClassList = tableClassRepository.findByCafeId(cafeId);
        List<Table> tableList = tableRepository.findByCafeId(cafeId);
        List<String> tableIdList = tableList.stream().map(Table::getId).collect(Collectors.toList());
        List<Reserved> reservedList = reservedRepository.findByTableIdIn(tableIdList);

        tableClassList.forEach(tableClass -> {
            managerReserveResDto.addTableClassSummaryDto(tableClass);
        });

        tableList.forEach(table -> {
            TableReserveDto tableReserveDto = new TableReserveDto();
            tableReserveDto.setTable(table);
            reservedList.stream()
                    .filter(reserved -> reserved.getTable().getId().equals(table.getId()))
                    .collect(Collectors.toList())
                    .forEach(reserved -> {
                        tableReserveDto.addReservePeriod(reserved);
                    });
            managerReserveResDto.addTableReserveDtoList(tableReserveDto);
        });
        return managerReserveResDto;
    }

    public void updateTable(String tableId, ManagerTableReqDto managerTableReqDto) {
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new CustomDBException("tableId에 해당하는 Table이 존재하지 않습니다."));

        table.update(managerTableReqDto.getName(), managerTableReqDto.getDescription(), managerTableReqDto.getImage());
        tableRepository.save(table);
    }

    public void addTable(ManagerTableReqDto managerTableReqDto) {
        TableClass tableClass = tableClassRepository.findById(managerTableReqDto.getTableClassId())
                .orElseThrow(() -> new CustomDBException("id에 해당하는 tableClass가 존재하지 않습니다."));

        Table table = Table.builder()
                .cafeId(tableClass.getCafeId())
                .tableClassId(tableClass.getId())
                .seating(tableClass.getSeating())
                .tableOptionList(tableClass.getTableOptionList())
                .build();

        table.update(managerTableReqDto.getName(), managerTableReqDto.getDescription(), managerTableReqDto.getImage());
        tableRepository.save(table);
    }

    public void updateTableClass(String tableClassId, ManagerTableClassReqDto managerTableClassReqDto) {
        TableClass tableClass = tableClassRepository.findById(tableClassId)
                .orElseThrow(() -> new CustomDBException("id에 해당하는 tableClass가 존재하지 않습니다."));

        tableClass.update(managerTableClassReqDto.getName(), managerTableClassReqDto.getSeating(), managerTableClassReqDto.getOption());
        tableClassRepository.save(tableClass);
    }

    public void addTableClass(String cafeId, ManagerTableClassReqDto managerTableClassReqDto) {

        TableClass tableClass = TableClass.builder()
                .cafeId(cafeId)
                .build();

        tableClass.update(managerTableClassReqDto.getName(), managerTableClassReqDto.getSeating(), managerTableClassReqDto.getOption());
        tableClassRepository.save(tableClass);
    }

    public ManagerLoginResDto join(ManagerLoginReqDto managerLoginReqDto) {

        if(cafeManagerRepository.existsByEmail(managerLoginReqDto.getEmail())) {
            throw new CustomDBException("email에 해당하는 cafeManager가 이미 존재합니다.");
        }

        CafeManager cafeManager = managerLoginReqDto.toEntity(passwordEncoder);
        CafeManager savedManager = cafeManagerRepository.save(cafeManager);
        return ManagerLoginResDto.builder()
                .id(savedManager.getId())
                .email(savedManager.getEmail())
                .role(savedManager.getRole().toString())
                .build();
    }
}
