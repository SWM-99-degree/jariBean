package com.example.jariBean.config.jwt;

import com.example.jariBean.config.auth.LoginCafe;
import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.config.jwt.jwtdto.JwtDto;
import com.example.jariBean.entity.CafeManager;
import com.example.jariBean.entity.Role;
import com.example.jariBean.entity.User;
import com.example.jariBean.handler.ex.CustomApiException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtProcess jwtProcess;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess) {
        super(authenticationManager);
        this.jwtProcess = jwtProcess;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = request.getHeader(JwtVO.ACCESS_HEADER);

        if(jwt != null) {
            JwtDto jwtDto = jwtProcess.verify(jwt);

            UserDetails userDetails = createUserDetails(jwtDto);

            // 임시 세션 강제 주입 (생명주기 request ~ response)
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        chain.doFilter(request, response);
    }

    public UserDetails createUserDetails(JwtDto jwtDto) {

        if(jwtDto.getRole().equals(Role.CUSTOMER.toString()) || jwtDto.getRole().equals(Role.UNREGISTERED.toString())) {
            User user = User.builder().id(jwtDto.getId()).role(Role.valueOf(jwtDto.getRole())).build();
            return new LoginUser(user);
        }

        if(jwtDto.getRole().equals(Role.MANAGER.toString())) {
            CafeManager cafeManager = CafeManager.builder().id(jwtDto.getId()).role(Role.valueOf(jwtDto.getRole())).build();
            return new LoginCafe(cafeManager);
        }

        if(jwtDto.getRole().equals(Role.UNREGISTERED.toString())) {
            throw new CustomApiException("회원가입을 진행하세요.");
        }

        throw new CustomApiException("해당하는 Role은 존재하지 않습니다.");
    }
}
