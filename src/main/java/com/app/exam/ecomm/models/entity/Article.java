package com.app.exam.ecomm.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable = false)
    private String reference;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private BigDecimal prix;

    @Column(name = "stock", nullable = false)
    private Integer quantity;

    private boolean deleted;

}