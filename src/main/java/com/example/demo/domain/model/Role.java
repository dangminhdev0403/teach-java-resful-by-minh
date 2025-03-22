package com.example.demo.domain.model;

import java.util.List;

import com.example.demo.domain.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Table(name = "roles")
public class Role extends BaseEntity {

    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
    private List<User> users;
}

