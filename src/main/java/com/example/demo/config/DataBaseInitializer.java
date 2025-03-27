package com.example.demo.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Role;
import com.example.demo.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataBaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

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

    }

}
