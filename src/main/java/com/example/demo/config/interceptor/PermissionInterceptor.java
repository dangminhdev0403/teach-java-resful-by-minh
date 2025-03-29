package com.example.demo.config.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import com.example.demo.domain.model.Permission;
import com.example.demo.domain.model.Role;
import com.example.demo.domain.model.User;
import com.example.demo.domain.response.ResponseData;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.util.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("null")
@RequiredArgsConstructor
@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    private final UserService userService;

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();

        Optional<String> usernameOptional = SecurityUtils.getCurrentUserLogin();
        String username = usernameOptional.orElse(null);
        if (username != null && !username.isEmpty()) {
            User user = this.userService.findByUsername(username);

            // ! RBAC

            List<Role> roles = user.getRoles();
            if (!roles.isEmpty()) {
                Permission currentPermission = permissionRepository.findByMethodAndPath(httpMethod, path);

                boolean isRole = roles.stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")
                        || role.getPermissions().contains(currentPermission));

                if (isRole) {
                    return true;
                } else {
                    response.setContentType("application/json;charset=UTF-8");

                    int statusCode = HttpStatus.FORBIDDEN.value();
                    response.setStatus(statusCode);
                    ResponseData<Object> data = ResponseData.<Object>builder()
                            .status(statusCode)
                            .data(null)
                            .error("403")
                            .message("Không có quyền hạn")
                            .build();

                    response.getWriter().write(objectMapper.writeValueAsString(data));
                    return false;
                }
            } else {
                response.setContentType("application/json;charset=UTF-8");

                int statusCode = HttpStatus.FORBIDDEN.value();
                response.setStatus(statusCode);
                ResponseData<Object> data = ResponseData.<Object>builder()
                        .status(statusCode)
                        .data(null)
                        .error("403")
                        .message("Không có quyền hạn")
                        .build();

                response.getWriter().write(objectMapper.writeValueAsString(data));
                return false;
            }

        }
        return true;

    }
}