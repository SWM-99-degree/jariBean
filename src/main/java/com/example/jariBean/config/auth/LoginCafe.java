package com.example.jariBean.config.auth;

import com.example.jariBean.entity.CafeManager;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
@Hidden
public class LoginCafe implements UserDetails {

    private final CafeManager cafeManager;

    public CafeManager getCafeManager() {
        return this.cafeManager;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> cafeManager.getRole().toString());
        return authorities;
    }

    @Override
    public String getPassword() {
        return cafeManager.getPassword();
    }

    @Override
    public String getUsername() {
        return cafeManager.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
