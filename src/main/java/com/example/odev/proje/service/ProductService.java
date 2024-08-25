package com.example.odev.proje.service;

import com.example.odev.proje.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findByCategoryId(Long categoryId);
    Product findById(int productId);
    public List<Product> searchProducts(String query);

}
