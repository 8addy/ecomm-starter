package com.app.exam.ecomm.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "articles_cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

    @Column(name = "cart_id")
    private Integer cartId;

    @Column(name = "prix")
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "pending")
    private boolean pending = true;

}
