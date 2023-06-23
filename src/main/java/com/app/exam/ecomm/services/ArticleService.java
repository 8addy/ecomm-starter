package com.app.exam.ecomm.services;

import com.app.exam.ecomm.models.dto.ArticleDTO;
import com.app.exam.ecomm.models.entity.Article;
import com.app.exam.ecomm.models.entity.ArticlesCart;
import com.app.exam.ecomm.repositories.ArticleRepository;
import com.app.exam.ecomm.repositories.ArticlesCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticlesCartRepository articlesCartRepository;

    public Page<Article> getAllArticles(Pageable pageable) {
        return articleRepository.findAllByDeletedFalse(pageable);
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findByIdAndDeletedFalse(id);
    }

    public Article saveArticle(ArticleDTO updateArticle, Long articleId) {
        Article existingArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));
        if (!existingArticle.getReference().equals(updateArticle.getReference())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La référence de l’article ne peut pas être modifiée.");
        }
        existingArticle.setDesignation(updateArticle.getDesignation());
        existingArticle.setPrix(updateArticle.getPrix());
        existingArticle.setQuantity(updateArticle.getQuantity());
        articleRepository.save(existingArticle);
        return existingArticle;
    }

    public ResponseEntity<?> deleteArticleById(Long id) {
        if (articleRepository.existsById(id)) {
            Boolean articlesCartExist = articlesCartRepository.existsPendingArticle(id);
            if (articlesCartExist) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete this article, it's still pending in cart");
            }
            Optional<Article> optionalArticle = articleRepository.findByIdAndDeletedFalse(id);
            if (optionalArticle.isPresent()) {
                Article article = optionalArticle.get();
                article.setDeleted(true);
                articleRepository.save(article);
                return ResponseEntity.ok("Article deleted successfully");
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found");
        }
    }

    public Article addArticle(ArticleDTO article) {
        Optional<Article> existingDesignation = articleRepository.findByDesignation(article.getDesignation());
        if(existingDesignation.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Article should have unique Designation");
        }
        Article newArticle = new Article();
        newArticle.setDesignation(article.getDesignation());
        newArticle.setReference(article.getReference());
        newArticle.setPrix(article.getPrix());
        newArticle.setQuantity(article.getQuantity());
        articleRepository.save(newArticle);
        return newArticle;
    }

    public List<ArticleDTO> searchArticlesByQuery(String query) {
        List<Article> articles = articleRepository.searchArticlesByDesignationAAndDeletedFalse(query);
        return articles.stream().map(article -> new ArticleDTO(article.getId(), article.getReference(), article.getDesignation(), article.getPrix(), article.getQuantity())).collect(Collectors.toList());
    }
}