package com.example.jariBean.repository.table;

import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;

import java.util.List;

public interface TableRepositoryTemplate {


    List<Table> findByConditions(String cafeId, Integer seating, List<TableClass.TableOption> tableOptionList);
}
