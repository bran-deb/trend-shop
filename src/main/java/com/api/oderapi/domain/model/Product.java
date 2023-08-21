package com.api.oderapi.domain.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;
}
