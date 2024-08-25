package com.example.odev.proje.service;

import com.example.odev.proje.dao.PastCartItemRepository;
import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.PastCartItem;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class PastCartItemServiceImpl implements PastCartItemService{
    @Autowired
    private PastCartItemRepository pastCartItemRepository;

    @Override
    public PastCartItem save(PastCartItem thepastcartitem) {
        return pastCartItemRepository.save(thepastcartitem);
    }

    @Override
    public List<PastCartItem> findByCart(Cart cart) {
        return pastCartItemRepository.findByCart(cart) ;
    }

//    @Override
//    @Transactional
//    public List<PastCartItem> findByUserId(long userId) {
//        return pastCartItemRepository.findByUserId(userId);
//    }


}
