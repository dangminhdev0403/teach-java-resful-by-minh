package com.example.demo.config;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.example.demo.domain.anotation.ApiDescription;
import com.example.demo.domain.model.Permission;
import com.example.demo.domain.model.Role;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataBaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RequestMappingHandlerMapping handlerMapping;

    public DataBaseInitializer(RoleRepository roleRepository, PermissionRepository permissionRepository,
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.handlerMapping = handlerMapping;
    }

    @Override
    public void run(String... args) throws Exception {
        long countRole = this.roleRepository.count();
        if (countRole == 0) {
            log.info("COUNT ROLE = 0 CREATE ROLE {}");
            String[] listRoleName = { "ROLE_ADMIN", "ROLE_USER", "ROLE_SELLER" };
            List<Role> listRole = Arrays.stream(listRoleName).map(roleName -> {
                Role role = new Role();
                role.setName(roleName);
                return role;
            }).toList();
            this.roleRepository.saveAll(listRole);
            log.info("SUCCESSFULLY CREATE LIST ROLE {}");

        }
        initializePermissions();
    }

    @Transactional
    private void initializePermissions() {
        long countPermission = permissionRepository.count();
        long countEndpoints = handlerMapping.getHandlerMethods().size();

        // Tập hợp tất cả các endpoint hiện có trong hệ thống Spring
        Set<String> existingEndpoints = handlerMapping.getHandlerMethods().entrySet().stream()
                .flatMap(entry -> {
                    var requestMappingInfo = entry.getKey();
                    var methods = requestMappingInfo.getMethodsCondition().getMethods();
                    var patternsCondition = requestMappingInfo.getPathPatternsCondition();

            
                    var patterns = (patternsCondition != null)
                            ? patternsCondition.getPatterns()
                            : Set.of(); // Tránh NullPointerException

                    return methods.stream()
                            .flatMap(m -> patterns.stream()
                                    .map(pattern -> m.name() + ":" + pattern.toString()));
                })
                .collect(Collectors.toSet());

        // Xóa các permissions không còn tồn tại trong Spring
        List<Permission> permissionsToDelete = permissionRepository.findAll().stream()
                .filter(permission -> !existingEndpoints.contains(permission.getMethod() + ":" + permission.getPath()))
                .toList();

        if (!permissionsToDelete.isEmpty()) {
            permissionRepository.deleteAll(permissionsToDelete);
            log.info("Deleted {} obsolete permissions.", permissionsToDelete.size());
        }

        // Thêm mới các permissions nếu thiếu
        if (countPermission != countEndpoints) {
            handlerMapping.getHandlerMethods().entrySet().stream()
                    .flatMap(entry -> {
                        var requestMappingInfo = entry.getKey();
                        var methods = requestMappingInfo.getMethodsCondition().getMethods();
                        var patternsCondition = requestMappingInfo.getPathPatternsCondition();

                        Method method = entry.getValue().getMethod();
                   ApiDescription apiDescription = method.getAnnotation(ApiDescription.class);

                        String description = apiDescription == null ?  method.getName(): apiDescription.value();

                        var patterns = (patternsCondition != null)
                                ? patternsCondition.getPatterns()
                                : Set.of(); // Tránh NullPointerException

                        return methods.stream()
                                .flatMap(m -> patterns.stream()
                                        .map(pattern -> new Permission(description, m.name(), pattern.toString())));
                    })
                    .distinct()
                    .filter(permission -> permissionRepository.findByMethodAndPath(permission.getMethod(),
                            permission.getPath()) == null)
                    .forEach(permissionRepository :: save);

            log.info("Permissions initialized from endpoints!");
        } else {
            log.info("Data is up to date, skipping initialization.");
        }
    }

}
