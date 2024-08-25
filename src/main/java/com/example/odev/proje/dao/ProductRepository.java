package com.example.odev.proje.dao;

import com.example.odev.proje.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:query%")
    List<Product> searchProducts(@Param("query") String query);
    //List<Product> findByNameContainingIgnoreCase(@Param("query")String name);
    //Product findById(Long productId);

}
