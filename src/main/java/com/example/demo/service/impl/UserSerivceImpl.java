package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSerivceImpl implements UserService {

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User save(User user) {

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hassPass = this.passwordEncoder.encode(user.getPassword());
            user.setPassword(hassPass);
        }
        User saveUser = this.userRepository.save(user);

        return saveUser;
    }

    @Override
    public User createUser(User user) {

        return this.save(user);
    }

    @Override
    public User updateUser(long id, User userFontend) {
        String email = userFontend.getEmail();
        String name = userFontend.getName();
        String password = userFontend.getPassword().isEmpty() ? null
                : this.passwordEncoder.encode(userFontend.getPassword());
        Optional<User> optional = this.userRepository.findById(id);
        if (optional.isPresent()) {
            User userDb = optional.get();

            userDb.setEmail(email);
            userDb.setName(name);
            userDb.setPassword(password);
            return this.userRepository.save(userDb);

        }

        return null;

    }

    @Override
    public List<User> getAllUsers() {
        List<User> listUser = this.userRepository.findAll();
        return listUser;
    }

    @Override
    public void deleteUser(Long id) {

        this.userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        User loginUser = new User();
        
            Optional<User> optional = this.userRepository.findByEmail(username);
            loginUser = optional.isPresent() ? optional.get() : null;
        
        return loginUser;
    }

    

}
