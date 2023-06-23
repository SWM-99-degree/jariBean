package com.example.jariBean.config.auth;

import com.example.jariBean.entity.Cafe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class LoginCafe implements UserDetails {

    private final Cafe user;

    public Cafe getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> user.getUserRole().toString());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getCafePassword();
    }


    @Override
    public String getUsername() {
        /**
         * username → password
         * password를 idnentifier로 사용한다.
         */
        return user.getCafePhoneNumber();
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
