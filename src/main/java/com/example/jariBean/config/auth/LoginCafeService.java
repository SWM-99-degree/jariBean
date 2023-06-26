package com.example.jariBean.config.auth;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.repository.cafe.CafeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginCafeService implements UserDetailsService {

    @Autowired
    CafeRepository cafeRepository;

    @Override
    public UserDetails loadUserByUsername(String userPhoneNumber) throws UsernameNotFoundException {
        Cafe cafe = cafeRepository.findByCafePhoneNumber(userPhoneNumber).orElseThrow(
                () -> new InternalAuthenticationServiceException("인증 실패")
        );
        return new LoginCafe(cafe);
    }
}
