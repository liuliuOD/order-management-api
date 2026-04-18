package io.github.liuliu.ordermanagement.controller;

import io.github.liuliu.api.ProductApi;
import io.github.liuliu.model.Product;
import io.github.liuliu.ordermanagement.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<Product> getProductById(UUID productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }
}
