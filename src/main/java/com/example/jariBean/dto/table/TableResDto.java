package com.example.jariBean.dto.table;

import java.util.List;

import static com.example.jariBean.entity.TableClass.TableOption;

public class TableResDto {

    public static class TableDetailDto {
        private String id;
        private String name;
        private Integer seating;
        private String image;
        private List<TableOption> tableOptionList;
    }
}
