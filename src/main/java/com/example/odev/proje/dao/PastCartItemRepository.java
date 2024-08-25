package com.example.odev.proje.dao;

import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.Cart_Item;
import com.example.odev.proje.entity.PastCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PastCartItemRepository extends JpaRepository<PastCartItem,Integer> {
    List<PastCartItem> findByCart(Cart cart);
    //List<PastCartItem> findByUserId( long userId);
}
