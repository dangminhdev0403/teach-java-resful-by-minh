package com.example.demo.service.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.util.error.InValidEmailException;

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
    public User createUser(User user) throws InValidEmailException {
        String email = user.getEmail();

        Optional<User> optional = this.userRepository.findByEmail(email);

        if (optional.isPresent()) {
            throw new InValidEmailException("Email đã tồn tại");
        }
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

    @Override
    public <T> Set<T> getAllUsers(Class<T> type) {
        return this.userRepository.findAllBy(type);
    }

    @Override
    public void updateRefreshToken(String email, String refreshToken) {

        Optional<User> optional = this.userRepository.findByEmail(email);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Override
    public <T> Optional<T> getUserByRefreshTokenAndEmail(Class<T> type, String email, String refreshToken) {

        return this.userRepository.findByEmailAndRefreshToken(type, email, refreshToken);
    }

}
