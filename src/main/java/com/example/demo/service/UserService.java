package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.model.User;

public interface UserService {
   User createUser(User user);

  User updateUser(long id ,User user);

    List<User> getAllUsers();

    void deleteUser(Long id);

    User findByUsername(String name);

   

}
