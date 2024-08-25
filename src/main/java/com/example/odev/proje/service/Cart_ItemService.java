package com.example.odev.proje.service;

import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.Cart_Item;

import java.util.List;

public interface Cart_ItemService {
    Cart_Item save(Cart_Item thecartitem);
 void deleteByCart(Cart cart);
    List<Cart_Item> findByCart(Cart cart);
    void deleteById(int theId);
    Cart_Item findById(int theId);
}
