package com.example.odev.proje.service;

import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.users;

import java.util.List;
import java.util.Optional;

public interface CartService {
    Cart findOrCreateCartByUserId(users theuser);
    void deleteActiveCarts();
    void updateCartStatusToCompleted(Cart cart);
    void updateCartStatusToShipped(Cart cart);
    Cart findById(Long theId);
    public List<Cart> findCompletedCartsByUser(users theUser);
    public List<Cart> findShippedCartsByUser(users theUser);
    Cart save(Cart cart);
}
