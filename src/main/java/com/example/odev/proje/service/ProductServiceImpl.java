package com.example.odev.proje.service;


import com.example.odev.proje.dao.ProductRepository;
import com.example.odev.proje.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository theproductRepository) {
        this.productRepository = theproductRepository;
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public Product findById(int productId) {
        Optional<Product> result = productRepository.findById(productId);
        Product theProduct=null;
        if(result.isEmpty()){
            throw new RuntimeException("id not found");
        }else{
            theProduct=result.get();
        }
        return theProduct;
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProducts(query.toLowerCase());
    }
}
