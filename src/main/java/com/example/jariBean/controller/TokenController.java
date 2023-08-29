package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.oauth.LoginResDto.LoginSuccessResDto;
import com.example.jariBean.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.example.jariBean.config.jwt.JwtVO.REFRESH_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    private final TokenService tokenService;

    @Operation(summary = "renew JWT", description = "api for renew JWT")
    @ApiResponse(
            responseCode = "200",
            description = "JWT 갱신 성공",
            content = @Content(schema = @Schema(implementation = LoginSuccessResDto.class))
    )
    @PatchMapping("/jwt")
    public ResponseEntity renewJWT(@AuthenticationPrincipal LoginUser loginUser, HttpServletRequest request) {
        String refreshJWT = request.getHeader(REFRESH_HEADER);
        LoginSuccessResDto loginSuccessResDto = tokenService.renewJWT(loginUser.getUser().getId(), loginUser.getUser().getRole(), refreshJWT);
        return new ResponseEntity<>(new ResponseDto<>(1, "JWT 갱신 성공", loginSuccessResDto), OK);
    }
}
