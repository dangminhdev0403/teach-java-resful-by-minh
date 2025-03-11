package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.model.User;
import com.example.demo.domain.request.LoginReq;
import com.example.demo.domain.response.ResLoginDTO;
import com.example.demo.service.UserService;
import com.example.demo.service.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final SecurityUtils securityUtils;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${minh.jwt.refresh-token.validity.in.seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@RequestBody LoginReq loginReq) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginReq.getEmail(), loginReq.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        User currentUser = this.userService.findByUsername(loginReq.getEmail());

        // Khởi tạo với giá trị mặc định
        ResLoginDTO resLoginDTO = ResLoginDTO.builder().build();
        ResLoginDTO.UserLogin userLogin = ResLoginDTO.UserLogin.builder().build();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            String name = currentUser.getName();
            userLogin = ResLoginDTO.UserLogin.builder().email(email).name(name).build();
        }

        String accessToken = this.securityUtils.createAccessToken(loginReq.getEmail(), userLogin);
        resLoginDTO = ResLoginDTO.builder()
                .accessToken(accessToken)
                .user(userLogin)
                .build();

        // ! Lưu refresh_token với cookie
        ResponseCookie cookie = ResponseCookie.from("refresh_token",
                accessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExpiration).build();
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).body(resLoginDTO);
    }

}
