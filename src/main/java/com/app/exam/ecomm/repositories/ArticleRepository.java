package com.app.exam.ecomm.repositories;

import com.app.exam.ecomm.models.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByDeletedFalse(Pageable pageable);

    Optional<Article> findByIdAndDeletedFalse(Long id);

    Optional<Article> findByDesignation(String designation);

    @Query("SELECT a FROM Article a WHERE a.designation LIKE %:searchTerm% AND a.deleted = false")
    List<Article> searchArticlesByDesignationAAndDeletedFalse(@Param("searchTerm") String searchTerm);

}
