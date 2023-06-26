package com.example.jariBean.config.jwt.jwtdto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDto {

    private String id;
    private String userRole;

    public JwtDto(String id, String userRole){
        this.id = id;
        this.userRole = userRole;
    }

}
