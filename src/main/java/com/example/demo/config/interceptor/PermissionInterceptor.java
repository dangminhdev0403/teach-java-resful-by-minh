package com.example.demo.config.interceptor;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import com.example.demo.domain.model.User;
import com.example.demo.domain.response.ResponseData;
import com.example.demo.service.UserService;
import com.example.demo.service.util.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("null")
@RequiredArgsConstructor
@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();

        Optional<String> usernameOptional = SecurityUtils.getCurrentUserLogin();
        String username = usernameOptional.orElse(null);
        if (username != null && !username.isEmpty()) {
            User user = this.userService.findByUsername(username);
          

        }
        return true;

    }
}