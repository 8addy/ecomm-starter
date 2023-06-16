package com.app.exam.ecomm.controllers.dto;

import com.app.exam.ecomm.models.entity.ArticlesCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequest {
    private List<ArticlesCart> articlesCart;
}
