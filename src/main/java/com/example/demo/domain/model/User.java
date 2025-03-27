package com.example.demo.domain.model;

import java.util.List;

import com.example.demo.domain.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Email chưa nhập")
    @NotBlank(message = "Email chưa nhập")
    private String email;

    @NotNull(message = "Password chưa nhập")
    @NotBlank(message = "Password chưa nhập")
    private String password;

    @Column(columnDefinition = "LONGTEXT")

    private String refreshToken;


    @ManyToMany
    @JoinTable(name = "user_has_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))

    private List<Role> roles;

}
