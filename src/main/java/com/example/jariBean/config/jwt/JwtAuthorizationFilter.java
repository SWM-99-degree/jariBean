package com.example.jariBean.config.jwt;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.config.jwt.jwtdto.JwtDto;
import com.example.jariBean.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(JwtVO.ACCESS_HEADER);

        if(isExist(header)) {
            String jwt = header.replace(JwtVO.TOKEN_PREFIX, ""); // "BEARER " 제거
            JwtDto jwtDto = JwtProcess.verify(jwt);

            User user = User.builder().id(jwtDto.getId()).userRole(User.UserRole.valueOf(jwtDto.getUserRole())).build();
            LoginUser loginUser = new LoginUser(user);
            // 임시 세션 강제 주입 (생명주기 request ~ response)
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        chain.doFilter(request, response);
    }

    private boolean isExist(String header) {
        return (header != null && header.startsWith(JwtVO.TOKEN_PREFIX));
    }
}
