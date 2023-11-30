package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.oauth.LoginCode;
import com.example.jariBean.dto.oauth.LoginResDto;
import com.example.jariBean.dto.user.UserResDto.UserInfoRespDto;
import com.example.jariBean.service.UserService;
import com.example.jariBean.service.oauth.OAuthKakaoService;
import com.example.jariBean.service.oauth.OAuthService;
import com.example.jariBean.service.oauth.OAuthServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final OAuthServiceFactory authServiceFactory;
    private final UserService userService;
    private OAuthService oAuthService;

    @Operation(summary = "find user me", description = "api for find user me")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 조회 성공",
            content = @Content(schema = @Schema(implementation = UserInfoRespDto.class))
    )
    @GetMapping("/me")
    public ResponseEntity findProfile(@AuthenticationPrincipal LoginUser loginUser) {
        UserInfoRespDto userInfo = userService.findUserInfo(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "회원정보 조회 성공", userInfo), OK);
    }

    @Operation(summary = "register user", description = "api for register user")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 회원가입 성공",
            content = @Content(schema = @Schema(implementation = UserInfoRespDto.class))
    )
    @PutMapping("/register")
    public ResponseEntity register(@AuthenticationPrincipal LoginUser loginUser) {
        UserInfoRespDto userInfo = userService.register(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", userInfo), OK);
    }

    @Operation(summary = "update user information", description = "api for update user information")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 정보 수정 성공",
            content = @Content(schema = @Schema(implementation = UserInfoRespDto.class))
    )
    @PatchMapping
    public ResponseEntity update(@AuthenticationPrincipal LoginUser loginUser,
                                 @RequestPart(name = "image", required = false) MultipartFile image,
                                 @RequestPart(name = "username", required = false) String username,
                                 @RequestPart(name = "description", required = false) String description) throws IOException {

        // id로 User를 조회한 후 username, description, image url 정보를 갱신
        UserInfoRespDto userInfoRespDto = userService.updateUserInfo(loginUser.getUser().getId(), username, description, "");

        return new ResponseEntity<>(new ResponseDto<>(1, "회원정보 수정 성공", userInfoRespDto), OK);
    }

    @Operation(summary = "social login", description = "api for social login")
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = LoginResDto.LoginSuccessResDto.class))
    )
    @PostMapping("/login/{registrationId}")
    public ResponseEntity login(@PathVariable("registrationId") String registrationId,
                                @RequestBody LoginCode loginCode) {
        // assign an oauthService corresponding to the registrationId
        oAuthService = authServiceFactory.get(registrationId);
        // get accessToken using code
        String accessToken = oAuthService.getAccessToken(loginCode.getCode());
        // get user information using accessToken
        OAuthKakaoService.SocialUserInfo socialUserInfo = oAuthService.getUserInfo(accessToken);
        // save or update oauth information
        LoginResDto.LoginSuccessResDto loginSuccessResDto = oAuthService.saveOrUpdate(socialUserInfo);
        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 성공", loginSuccessResDto), OK);
    }

    @Operation(summary = "Canceling your account", description = "api for Canceling your account")
    @ApiResponse(
            responseCode = "200",
            description = "계정 탈퇴 성공",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @DeleteMapping
    public ResponseEntity withdraw(@AuthenticationPrincipal LoginUser loginUser,
                                   @Valid @RequestBody(required = false) LoginCode loginCode) {
        // find user information and extract registration
        String registration = userService.findUserInfo(loginUser.getUser().getId()).getSocialId();

        // allocate implemented oauth service
        oAuthService = authServiceFactory.get(registration);

        // delete user information
        oAuthService.deleteUser(loginUser.getUser().getId(), (loginCode != null) ? loginCode.getCode() : null);

        return new ResponseEntity<>(new ResponseDto<>(1, "계정 탈퇴 성공", null), OK);
    }

}
