package com.example.demo.config.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import com.example.demo.domain.model.User;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        log.info(">>> RUN preHandle");
        log.info(">>> path= " + path);
        log.info(">>> httpMethod= " + httpMethod);
        log.info(">>> requestURI= " + requestURI);

        String username = SecurityUtils.getCurrentUserLogin().get();
        if (username != null && !username.isEmpty()) {
            User user = this.userService.findByUsername(username);
            if (user != null) {
                user.getRole();
                if (path.contains("admin") && user.getRole().getName().equals("ROLE_SELLER")
                        && !httpMethod.equals("GET")) {
                    return false;
                } else {
                    return true;
                }
            }

        }
        return true;

    }
}