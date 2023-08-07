package com.example.jariBean.service;

import com.example.jariBean.dto.manager.ManagerReqDto.ManagerTableClassReqDto;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.tableClass.TableClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.jariBean.dto.manager.ManagerReqDto.ManagerTableReqDto;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final CafeRepository cafeRepository;
    private final TableRepository tableRepository;
    private final TableClassRepository tableClassRepository;

    public void toggleMatchingStatus(String id) {
        cafeRepository.findById(id)
                .orElseThrow(() -> new CustomDBException("id에 해당하는 Cafe가 존재하지 않습니다."))
                        .toggleMatchingStatus();
    }

    public void getReservePage() {

    }

    public void getTablePage() {

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
}
