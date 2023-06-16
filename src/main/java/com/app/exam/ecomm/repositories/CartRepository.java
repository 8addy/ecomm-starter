package com.app.exam.ecomm.repositories;

import com.app.exam.ecomm.models.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.articlesCart ac WHERE ac.pending = true AND c.active = true AND c.userId = :userId")
    Optional<Cart> findActiveCartWithPendingArticlesByUserId(Long userId);
}
