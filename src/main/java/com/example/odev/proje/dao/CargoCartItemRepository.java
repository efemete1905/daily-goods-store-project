package com.example.odev.proje.dao;

import com.example.odev.proje.entity.CargoCartItem;
import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.PastCartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargoCartItemRepository extends JpaRepository<CargoCartItem,Integer> {

List<CargoCartItem> findByCart(Cart cart);
@Transactional
    void deleteByCart(Cart cart);
}
