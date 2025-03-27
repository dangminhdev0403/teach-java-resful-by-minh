package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByMethodAndPath(String method, String path);
}
