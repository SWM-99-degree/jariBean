package com.example.jariBean.dto.table;

import com.example.jariBean.entity.Table;

import java.util.List;

import static com.example.jariBean.entity.TableClass.TableOption;

public class TableResDto {

    public static class TableDetailDto {
        private String id;
        private String name;
        private Integer seating;
        private String image;
        private List<TableOption> tableOptionList;

        public TableDetailDto(Table table){
            this.id = table.getId();
            this.name = table.getDescription(); //TODO
            this.seating = table.getSeating();
            this.image = table.getImageUrl();
            this.tableOptionList = table.getTableOptionList();
        }
    }
}
