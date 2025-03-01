package com.example.demo.domain.model;

import com.example.demo.domain.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    private String email;
    private String password;

}
