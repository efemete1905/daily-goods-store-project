package com.example.odev.proje.service;

import com.example.odev.proje.dao.Cart_ItemRepository;
import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.Cart_Item;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

@Service
public class Cart_ItemServiceImpl implements Cart_ItemService{
    private Cart_ItemRepository cartItemRepository;

    public Cart_ItemServiceImpl(Cart_ItemRepository thecartItemRepository) {
        this.cartItemRepository = thecartItemRepository;
    }


    @Override
    public Cart_Item save( Cart_Item thecartitem) {
        return cartItemRepository.save(thecartitem);
    }
    public void deleteByCart(Cart cart) {
        cartItemRepository.deleteByCart(cart);
    }

    @Override
    public List<Cart_Item> findByCart(Cart cart) {
        return cartItemRepository.findByCart(cart);
    }
    public Cart_Item findById(int theId) {
        Optional<Cart_Item> result = cartItemRepository.findById(theId);
        Cart_Item theCartItem=null;
        if(result.isEmpty()){
            throw new RuntimeException("id not found");
        }else{
            theCartItem=result.get();
        }
        return theCartItem;
    }
public void deleteById(int theId){
cartItemRepository.deleteById(theId);
}
}
