package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.model.User;
import com.example.demo.domain.request.LoginReq;
import com.example.demo.domain.response.ResLoginDTO;
import com.example.demo.service.UserService;
import com.example.demo.service.util.SecurityUtils;

import jakarta.validation.Valid;
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
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginReq loginReq) {

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

        // ! Nạp thông tin hoi vào SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ! Tạo refresh token
        String refresh_token = this.securityUtils.createRefreshToken(loginReq.getEmail(), resLoginDTO);

        // lưu refresh token
        this.userService.updateRefreshToken(loginReq.getEmail(), refresh_token);

        // ! Lưu refresh_token với cookie
        ResponseCookie cookie = ResponseCookie.from("refresh_token",
                refresh_token)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExpiration).build();
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).body(resLoginDTO);
    }

    @GetMapping("/refresh")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken)
            throws Exception {

        if (refreshToken == null || refreshToken.isEmpty()) {

            throw new Exception("Không tìm thấy refresh token");

        }

        // ! Kiem tra refresh token
        Jwt decodedToken = this.securityUtils.checkValidRefreshToken(refreshToken);

        String email = decodedToken.getSubject();

        // ! Tìm refresh token trong database
        Optional<User> currentUser = this.userService.getUserByRefreshTokenAndEmail(User.class, email, refreshToken);

        if (!currentUser.isPresent()) {
            throw new Exception("Không tìm thấy refresh token");
        }


        //! Tao token mới 
        String name = currentUser.get().getName();

        ResLoginDTO.UserLogin userLogin = ResLoginDTO.UserLogin.builder().email(email).name(name).build();
        String access_token = this.securityUtils.createAccessToken(email, userLogin);
        ResLoginDTO resLoginDTO = ResLoginDTO.builder().user(userLogin).accessToken(access_token).build();

        String refresh_token = this.securityUtils.createRefreshToken(email, resLoginDTO);

        this.userService.updateRefreshToken(email, refresh_token);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExpiration).build();
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).body(resLoginDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<String> resgister(@Valid @RequestBody User userRes) {

        return ResponseEntity.ok("Đăng kí thành công");
    }

}
