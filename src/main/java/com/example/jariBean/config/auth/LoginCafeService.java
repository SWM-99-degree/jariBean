package com.example.jariBean.config.auth;

import com.example.jariBean.entity.CafeManager;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.cafemanager.CafeManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginCafeService implements UserDetailsService {

    @Autowired CafeManagerRepository cafeManagerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CafeManager cafeManager = cafeManagerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomApiException("email에 해당하는 cafe manager가 존재하지 않습니다."));
        return new LoginCafe(cafeManager);
    }
}
