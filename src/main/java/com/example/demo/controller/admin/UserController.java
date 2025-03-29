package com.example.demo.controller.admin;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.domain.anotation.ApiDescription;
import com.example.demo.domain.model.User;
import com.example.demo.domain.specs.UserSpes;
import com.example.demo.service.UserService;
import com.example.demo.service.util.error.InValidEmailException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User userFontend) throws InValidEmailException {

        User saveUser = this.userService.createUser(userFontend);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveUser.getId()) // Giả sử User có phương thức getId()
                .toUri();

        return ResponseEntity.created(location).body(saveUser); // ! 201
    }

    @GetMapping("")
    @ApiDescription("Lấy danh sách người dùng")
    public ResponseEntity<List<User>> getListUser(
            @RequestParam("name") Optional<String> name,
            @RequestParam("email") Optional<String> email,
            Pageable pageable) {

        String nameValue = name.orElse("");
        String emailValue = email.orElse("");

        // Page<UserDTO> pageUser =
        // this.userService.getAllUsers(UserDTO.class,pageable);

        Specification<User> specification = Specification.where(null);

        specification = specification.and(UserSpes.likeName(nameValue)).and(UserSpes.emailLike(emailValue));

        Page<User> pageUser = this.userService.getAllUsers(specification, pageable);

        List<User> listUsers = pageUser.getContent();

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

    @GetMapping("/")
    public ResponseEntity<User> getUserByEmail(@RequestParam(value = "email") String email) {

        User user = this.userService.findByUsername(email);

        return ResponseEntity.ok(user);
    }

}
