package com.app.exam.ecomm.repositories;

import com.app.exam.ecomm.models.entity.ArticlesCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticlesCartRepository extends JpaRepository<ArticlesCart, Integer> {
    @Query("SELECT COUNT(a) > 0 FROM ArticlesCart a WHERE a.pending = true AND a.article.id = :articleId")
    boolean existsPendingArticle(@Param("articleId") Long articleId);

    @Query("SELECT a FROM ArticlesCart a WHERE a.pending = true AND a.cartId = :cartId")
    List<ArticlesCart> finByCartIdAndPendingTrue(Integer cartId);
}
