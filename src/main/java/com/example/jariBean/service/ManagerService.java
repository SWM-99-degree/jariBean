package com.example.jariBean.service;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Table;
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

    public boolean toggleMatchingStatus(String id) {
        Cafe findCafe = cafeRepository.findById(id)
                .orElseThrow(() -> new CustomDBException("id에 해당하는 Cafe가 존재하지 않습니다."));

        boolean status = findCafe.toggleMatchingStatus();

        return status;
    }

    public void addTable(ManagerTableReqDto managerTableReqDto) {
        Table.
    }

    public void modifyTable() {

    }

    public void addTableClass() {

    }

    public void modifyTableClass() {

    }
}
