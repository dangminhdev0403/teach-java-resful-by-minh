package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
  

    @Override
    public void deleteRole(Long id) {
        
        this.roleRepository.deleteById(id);
    }

}
