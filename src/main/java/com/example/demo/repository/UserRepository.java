package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    <T> Set<T> findAllBy(Class<T> type);
    
    <T> Optional<T> findByEmailAndRefreshToken(Class<T> type, String email, String refreshToken);
  

}