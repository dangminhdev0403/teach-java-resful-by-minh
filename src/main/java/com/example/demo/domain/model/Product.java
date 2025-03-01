package com.example.demo.domain.model;

import com.example.demo.domain.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private double price;
}
