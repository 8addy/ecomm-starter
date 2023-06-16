package com.app.exam.ecomm.controllers;

import com.app.exam.ecomm.models.dto.ArticleDTO;
import com.app.exam.ecomm.models.entity.Article;
import com.app.exam.ecomm.response.ErrorMessage;
import com.app.exam.ecomm.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articles = articleService.getAllArticles(pageable);
        List<ArticleDTO> listArticles = articles.stream().map(article -> new ArticleDTO(article.getId(), article.getReference(), article.getDesignation(), article.getPrix(), article.getQuantity())).collect(Collectors.toList());
        return ResponseEntity.ok(listArticles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable("id") Long id) {
        Optional<Article> articleOptional = articleService.getArticleById(id);

        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            ArticleDTO articleDTO = new ArticleDTO(article.getId(), article.getReference(), article.getDesignation(), article.getPrix(), article.getQuantity());
            return ResponseEntity.ok(articleDTO);
        } else {
            ErrorMessage errorMessage = new ErrorMessage("Article not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Article> addArticle(@RequestBody ArticleDTO article) {
        return ResponseEntity.ok(articleService.addArticle(article));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") Long id, @RequestBody ArticleDTO article) {
        Article updatedArticle = articleService.saveArticle(article, id);
        return ResponseEntity.ok(updatedArticle);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable("id") Long id) {
        return articleService.deleteArticleById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleDTO>> searchArticlesByQuery(@RequestParam("query") String query) {
        List<ArticleDTO> articles = articleService.searchArticlesByQuery(query);
        return ResponseEntity.ok(articles);
    }

}
