package com.example.odev.proje.service;

import com.example.odev.proje.dao.CargoCartItemRepository;
import com.example.odev.proje.dao.CartRepository;
import com.example.odev.proje.dao.PastCartItemRepository;
import com.example.odev.proje.entity.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service

public class CartServiceImpl implements CartService {
    @Autowired
    private CargoCartItemService cargoCartItemService;
    private final CartRepository cartRepository;
    @Autowired
    private Cart_ItemService cartItemService;
    @Autowired
    private PastCartItemRepository pastCartItemRepository;
    @Autowired
    private CargoCartItemRepository cargoCartItemRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart findOrCreateCartByUserId(users theuser) {
        List<Cart> activeCarts = cartRepository.findByUserIdAndStatus(theuser.getId(), "active");
        if (!activeCarts.isEmpty()) {
            // Kullanıcının aktif bir sepeti varsa, ilk sepeti döndür
            return activeCarts.get(0);
        } else {
            // Kullanıcının aktif bir sepeti yoksa, yeni bir sepet oluştur ve kaydet
            Cart newCart = new Cart(theuser, "active", 0.0); // 0.0 varsayılan değer olarak eklenmiş
            return cartRepository.save(newCart);
        }
    }

    @Override
    public void deleteActiveCarts() {
        List<Cart> activeCarts=cartRepository.findByStatus("active");
        for (Cart carts : activeCarts){
            cartItemService.deleteByCart(carts);
            cartRepository.delete(carts);
        }
    }

    @Override
    @Transactional
    public void updateCartStatusToCompleted(Cart cart) {
        List<CargoCartItem> cartItems = cargoCartItemService.findByCart(cart);
        List<PastCartItem> pastCartItems = new ArrayList<>();
        for (CargoCartItem cartItem : cartItems) {
            PastCartItem pastCartItem = new PastCartItem();
            pastCartItem.setCart(cart);
            pastCartItem.setProduct(cartItem.getProduct());
            pastCartItem.setQuantity(cartItem.getQuantity());
            pastCartItem.setPrice(cartItem.getPrice());
            pastCartItem.setTotalPrice(cartItem.getTotalPrice());
            pastCartItems.add(pastCartItem);
            cargoCartItemRepository.deleteByCart(cart);
        }
        pastCartItemRepository.saveAll(pastCartItems);

        cartItemService.deleteByCart(cart);
        cart.setStatus("completed");
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void updateCartStatusToShipped(Cart cart) {
        List<Cart_Item> cartItems = cartItemService.findByCart(cart);
        List<CargoCartItem> cargoCartItems = new ArrayList<>();
        for (Cart_Item cartItem : cartItems) {
            CargoCartItem cargoCartItem = new CargoCartItem();
            cargoCartItem.setCart(cart);
            cargoCartItem.setProduct(cartItem.getProduct());
            cargoCartItem.setQuantity(cartItem.getQuantity());
            cargoCartItem.setPrice(cartItem.getPrice());
            cargoCartItem.setTotalPrice(cartItem.getTotalPrice());
            cargoCartItems.add(cargoCartItem);
        }
        cargoCartItemRepository.saveAll(cargoCartItems);

        cartItemService.deleteByCart(cart);
        cart.setStatus("shipped");
        cartRepository.save(cart);

    }

    @Override
    public Cart findById(Long theId) {
        return cartRepository.findById(theId);
    }

    @Override
    public List<Cart> findCompletedCartsByUser(users theUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (!theUser.getUsername().equals(currentPrincipalName)) {
            throw new IllegalStateException("Current user is not authorized to access this information.");
        }

        return Optional.ofNullable(cartRepository.findByUserIdAndStatus(theUser.getId(), "completed"))
                .orElseThrow(() -> new IllegalStateException("Completed carts not found for the user."));
    }

    @Override
    public List<Cart> findShippedCartsByUser(users theUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (!theUser.getUsername().equals(currentPrincipalName)) {
            throw new IllegalStateException("Current user is not authorized to access this information.");
        }

        return Optional.ofNullable(cartRepository.findByUserIdAndStatus(theUser.getId(), "shipped"))
                .orElseThrow(() -> new IllegalStateException("Completed carts not found for the user."));
    }


    @Override
    @Transactional
    public Cart save(Cart cart) {
        return cartRepository.save(cart);

    }

}


