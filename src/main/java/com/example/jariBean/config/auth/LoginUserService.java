package com.example.jariBean.config.auth;

import com.example.jariBean.entity.User;
import com.example.jariBean.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService implements UserDetailsService {

    @Autowired UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPhoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByUserPhoneNumber(userPhoneNumber).orElseThrow(
                () -> new InternalAuthenticationServiceException("인증 실패")
        );
        return new LoginUser(user);
    }


}
