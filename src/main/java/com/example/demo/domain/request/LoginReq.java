package com.example.demo.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReq {

    @NotNull(message = "Email chưa nhập")
    @NotBlank(message = "Email chưa nhập")
    @Email(message = "Email khóng hợp lệ")
    private String email;

    @NotNull(message = "Password chưa nhập")
    @NotBlank(message = "Password chưa nhập")
    private String password;

}