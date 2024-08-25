package com.example.odev.proje.dao;

import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.Cart_Item;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Cart_ItemRepository extends JpaRepository<Cart_Item,Integer> {
    @Transactional
    void deleteByCart(Cart cart);
    List<Cart_Item> findByCart(Cart cart);


}
