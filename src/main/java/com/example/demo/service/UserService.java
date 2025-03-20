package com.example.demo.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.model.User;
import com.example.demo.service.util.error.InValidEmailException;

public interface UserService {
  User createUser(User user) throws InValidEmailException;

  User updateUser(long id, User user);

  <T> Set<T> getAllUsers(Class<T> type);

  <T> Page<T> getAllUsers(Class<T> type, Pageable pageable);

  Page<User> getAllUsers(Specification<User> specification, Pageable pageable);

  void deleteUser(Long id);

  User findByUsername(String name);

  void updateRefreshToken(String email, String refreshToken);

  <T> Optional<T> getUserByRefreshTokenAndEmail(Class<T> type, String email, String refreshToken);

}
