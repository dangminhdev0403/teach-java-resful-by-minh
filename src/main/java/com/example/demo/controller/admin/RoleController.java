package com.example.demo.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> updateRole(@PathVariable long id) {
        this.roleService.deleteRole(id);

        return ResponseEntity.ok("xoá thành công");
    }

}
