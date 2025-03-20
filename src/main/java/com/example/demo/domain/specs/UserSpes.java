package com.example.demo.domain.specs;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.domain.model.User;
import com.example.demo.domain.model.User_;

public class UserSpes {
    public static Specification<User> likeName(String name) {
        return (root, query, builder) -> builder.like(root.get(User_.name), "%" + name + "%");
    }

    public static Specification<User> emailLike(String email) {
        return (root, query, builder) -> builder.like(root.get(User_.EMAIL), "%" + email + "%");
    }
}
