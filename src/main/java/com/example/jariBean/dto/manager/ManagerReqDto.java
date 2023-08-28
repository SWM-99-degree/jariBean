package com.example.jariBean.dto.manager;

import com.example.jariBean.entity.CafeManager;
import com.example.jariBean.entity.TableClass.TableOption;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

import static com.example.jariBean.entity.Role.MANAGER;

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

    @Data
    @NoArgsConstructor
    public static class ManagerJoinReqDto {
        @Email
        @NotEmpty(message = "id는 필수입니다.")
        private String email;

        @NotEmpty(message = "password는 필수입니다.")
        private String password;

        private String cafeId;

        @Builder
        public ManagerJoinReqDto(String email, String password, String cafeId) {
            this.email = email;
            this.password = password;
            this.cafeId = cafeId;
        }

        public CafeManager toEntity(PasswordEncoder passwordEncoder) {
            return CafeManager.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .cafeId(cafeId)
                    .role(MANAGER)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ManagerLoginReqDto {
        @Email
        @NotEmpty(message = "id는 필수입니다.")
        private String email;

        @NotEmpty(message = "password는 필수입니다.")
        private String password;

        @Builder
        public ManagerLoginReqDto(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

}
