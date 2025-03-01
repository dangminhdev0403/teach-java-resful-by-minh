package com.example.demo.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.model.User;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User userFontend) {
       
        User saveUser = this.userService.createUser(userFontend);

        return ResponseEntity.created(null).body(saveUser); // ! 201
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getListUser() {

        List<User> listUsers = this.userService.getAllUsers();

        return ResponseEntity.ok(listUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User userFontend) {
        User updateUser = this.userService.updateUser(id, userFontend);
        if (updateUser == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id) {
       this.userService.deleteUser(id);
       
        return ResponseEntity.ok("xoá thành công");
    }

}
