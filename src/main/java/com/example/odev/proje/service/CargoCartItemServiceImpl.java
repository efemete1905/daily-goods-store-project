package com.example.odev.proje.service;


import com.example.odev.proje.dao.CargoCartItemRepository;
import com.example.odev.proje.entity.CargoCartItem;
import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.Cart_Item;
import com.example.odev.proje.entity.PastCartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoCartItemServiceImpl implements  CargoCartItemService{
    @Autowired
    CargoCartItemRepository cargoCartItemRepository;
    @Override
    public CargoCartItem save(CargoCartItem thecargocartitem) {
        return cargoCartItemRepository.save(thecargocartitem);
    }

    @Override
    public List<CargoCartItem> findByCart(Cart cart) {
        return cargoCartItemRepository.findByCart(cart) ;
    }

    public void deleteByCart(Cart cart) {
        cargoCartItemRepository.deleteByCart(cart);
    }

    @Override
    public void deleteById(int theId) {
        cargoCartItemRepository.deleteById(theId);

    }

    @Override
    public CargoCartItem findById(int theId) {
        Optional<CargoCartItem> result = cargoCartItemRepository.findById(theId);
        CargoCartItem theCargoCartItem=null;
        if(result.isEmpty()){
            throw new RuntimeException("id not found");
        }else{
            theCargoCartItem=result.get();
        }
        return theCargoCartItem;
    }


}
