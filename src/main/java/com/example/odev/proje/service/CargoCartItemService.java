package com.example.odev.proje.service;

import com.example.odev.proje.entity.CargoCartItem;
import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.Cart_Item;
import com.example.odev.proje.entity.PastCartItem;

import java.util.List;

public interface CargoCartItemService {
    CargoCartItem save(CargoCartItem thecargocartitem);
    public List<CargoCartItem> findByCart(Cart cart);

    public void deleteByCart(Cart cart);


    void deleteById(int theId);
    CargoCartItem findById(int theId);

}
