package com.example.jariBean.dto.manager;

import com.example.jariBean.entity.TableClass.TableOption;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ManagerReqDto {

    @Data
    public static class ManagerTableReqDto {

        @NotEmpty(message = "name은 필수입니다.")
        private String tableClassId;

        @NotEmpty(message = "name은 필수입니다.")
        @Size(min = 1, max = 20)
        private String name;

        @NotEmpty(message = "description은 필수입니다.")
        @Size(min = 1, max = 50)
        private String description;

        private String image;
    }

    @Data
    public static class ManagerTableClassReqDto {

        @NotEmpty(message = "description은 필수입니다.")
        @Size(min = 1, max = 50)
        private String name;

        @Positive(message = "seating은 필수입니다.")
        private Integer seating;

        private List<TableOption> option;
    }

}
