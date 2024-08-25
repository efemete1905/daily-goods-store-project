package com.example.odev.proje.service;

import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.Cart_Item;
import com.example.odev.proje.entity.PastCartItem;

import java.util.List;

public interface PastCartItemService {
    PastCartItem save(PastCartItem thepastcartitem);
    public List<PastCartItem> findByCart(Cart cart);
    //public  List<PastCartItem> findByUserId(long userId);
}
