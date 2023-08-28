package com.example.jariBean.config.jwt.jwtdto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDto {

    private String id;
    private String role;

    public JwtDto(String id, String role){
        this.id = id;
        this.role = role;
    }

}
