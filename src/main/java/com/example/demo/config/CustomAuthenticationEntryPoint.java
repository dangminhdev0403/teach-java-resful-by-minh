package com.example.demo.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo.domain.response.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component("customAuthenticationEntryPoint")
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // ! sẽ dịch cái data sang string

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        String path = request.getRequestURI();
                
        int statusCode = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(statusCode);
        ResponseData<Object> data = ResponseData.<Object>builder().status(
                statusCode).data(null).error("401").message("Token không hợp lệ").build();

        response.getWriter().write(objectMapper.writeValueAsString(data));

    }

}
