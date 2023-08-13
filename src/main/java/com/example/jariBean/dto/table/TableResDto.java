package com.example.jariBean.dto.table;

import com.example.jariBean.entity.Table;

import java.util.List;

import static com.example.jariBean.entity.TableClass.TableOption;

public class TableResDto {

    public static class TableDetailDto {
        private String id;
        private String description;
        private Integer seating;
        private String image;
        private List<TableOption> tableOptionList;

        public TableDetailDto(Table table){
            this.id = table.getId();
            this.description = table.getDescription();
            this.seating = table.getSeating();
            this.image = table.getImage();
            this.tableOptionList = table.getTableOptionList();
        }
    }
}
