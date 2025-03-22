package com.example.demo.domain.model;

import com.example.demo.domain.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
