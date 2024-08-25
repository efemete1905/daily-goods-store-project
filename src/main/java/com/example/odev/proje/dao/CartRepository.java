package com.example.odev.proje.dao;

import com.example.odev.proje.entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    List<Cart> findByUserIdAndStatus(double userId, String status);
    List<Cart> findByStatus(String status);
    Cart findById(Long id);


    @Transactional
    void delete(Cart cart);
}
