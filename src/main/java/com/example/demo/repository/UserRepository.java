package com.example.demo.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    <T> Set<T> findAllBy(Class<T> type);

    <T> Page<T> findAllBy(Class<T> type, Pageable pageable);

    <T> Optional<T> findByEmailAndRefreshToken(Class<T> type, String email, String refreshToken);

    

}