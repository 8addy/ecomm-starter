package com.app.exam.ecomm.services;

import com.app.exam.ecomm.controllers.dto.AddToCartRequest;
import com.app.exam.ecomm.models.entity.Article;
import com.app.exam.ecomm.models.entity.ArticlesCart;
import com.app.exam.ecomm.models.entity.Cart;
import com.app.exam.ecomm.models.entity.User;
import com.app.exam.ecomm.repositories.ArticleRepository;
import com.app.exam.ecomm.repositories.ArticlesCartRepository;
import com.app.exam.ecomm.repositories.CartRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ArticlesCartRepository articlesCartRepository;
    private final ArticleRepository articleRepository;
    private final UserService userService;
    public Optional<Cart> getCart() {
        User user = userService.getAuthenticatedUser();
        return cartRepository.findActiveCartWithPendingArticlesByUserId(user.getId());
    }

    @Transactional
    public ResponseEntity<?> addToCart(Integer cartId, AddToCartRequest request) {
        Cart cart;

        if (cartId != null) {
            // Check if the Cart entity exists with the given ID
            Optional<Cart> optionalCart = cartRepository.findById(cartId);

            if (optionalCart.isPresent()) {
                // If the Cart entity exists, retrieve it from the database
                cart = optionalCart.get();
            } else {
                // If the Cart entity does not exist, throw an exception or handle the situation as needed
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found with ID: " + cartId);
            }
        } else {
            cart = new Cart();
            User user = userService.getAuthenticatedUser();
            cart.setActive(true);
            cart.setUserId(user.getId());
            // Save the cart entity to generate the cart ID
            cart = cartRepository.save(cart);
        }

        List<ArticlesCart> articlesCart = request.getArticlesCart();

        // Set the cartId for each ArticlesCart entity
        for (ArticlesCart currArticle : articlesCart) {
            // Check if the articles quanity is available in stock
            Optional<Article> optionalArticle = articleRepository.findByIdAndDeletedFalse(currArticle.getArticle().getId());
            if (optionalArticle.isPresent()) {
                Article article = optionalArticle.get();
                if (currArticle.getQuantity() > article.getQuantity()) {
                    throw new IllegalArgumentException("The requested quantity exceeds the available stock. Please choose a lower quantity for Article: " + article.getId());
                } else {
                    currArticle.setCartId(cart.getId());
                    article.setQuantity(article.getQuantity() - currArticle.getQuantity());
                    articleRepository.save(article);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with ID: " + currArticle.getArticle().getId());
            }
        }

        // Save the list of ArticlesCart entities
        articlesCartRepository.saveAll(request.getArticlesCart());
        return ResponseEntity.ok(cart);
    }
}
