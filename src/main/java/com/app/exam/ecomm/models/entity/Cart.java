package com.app.exam.ecomm.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "cartId", cascade = CascadeType.ALL)
    private List<ArticlesCart> articlesCart = new ArrayList<>();

    @Column(name = "active")
    private boolean active;


    @Column(name = "user_id")
    private Long userId;
}
