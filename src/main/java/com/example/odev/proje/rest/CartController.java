package com.example.odev.proje.rest;

import com.example.odev.proje.dao.usersRepository;
import com.example.odev.proje.entity.*;
import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.service.LogService;
import com.example.odev.proje.service.CartService;
import com.example.odev.proje.service.Cart_ItemService;
import com.example.odev.proje.service.MyUserDetailsService;
import com.example.odev.proje.service.ProductService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
    public class CartController {
        private final CartService cartService;
        private final Cart_ItemService  cartItemService;
        private final ProductService productService;
        private final MyUserDetailsService myUserDetailsService;
        private final usersRepository usersRepository;
        private final LogService logService;

//        @Autowired
//        public CartController(Cart_ItemService thecartItemService, ProductService theproductService, CartService theCartService,MyUserDetailsService themyUserDetailsService,usersRepository theUsersRepository) {
//            this.cartItemService = thecartItemService;
//            this.productService=theproductService;
//            this.cartService=theCartService;
//            this.myUserDetailsService=themyUserDetailsService;
//            this.usersRepository=theUsersRepository;
//        }
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity, Model model) {
        if (quantity <= 0) {
            model.addAttribute("error", "Lütfen 0'dan büyük bir miktar girin.");
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            String logMessage = "Adding product's quantity is 0 or less than 0 to cart user: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("ERROR");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);
            return "su-ve-icecek"; // hatayı gösterecek HTML sayfasına geri dön



        }

            Product product = productService.findById(productId);

        if (product != null) {
            double price = product.getPrice();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            users currentUser = usersRepository.findByUsername(currentPrincipalName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));


            Cart cart = cartService.findOrCreateCartByUserId(currentUser);
            double newTotalAmount = cart.getTotalAmount() + (price * quantity);
            cart.setTotalAmount(newTotalAmount);

            Cart_Item cartItem = new Cart_Item(cart, product, quantity);

            String username = authentication.getName();
            String logMessage = "Adding product ("+ product.getName() +") and its quantity is "+quantity+" to cart user: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("INFO");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);

            cartItemService.save(cartItem);
        }

        return "redirect:/su-ve-icecek";
    }
    @PostMapping("/addToCart1p")
    public String productaddToCart(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity) {
        Product product = productService.findById(productId);

        if (product != null) {
            double price = product.getPrice();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            users currentUser = usersRepository.findByUsername(currentPrincipalName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));


            Cart cart = cartService.findOrCreateCartByUserId(currentUser);
            double newTotalAmount = cart.getTotalAmount() + (price * quantity);
            cart.setTotalAmount(newTotalAmount);

            Cart_Item cartItem = new Cart_Item(cart, product, quantity);
            String username = authentication.getName();
            String logMessage = "Adding product ("+ product.getName() +") and its quantity is "+quantity+" to cart user: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("INFO");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);

            cartItemService.save(cartItem);
        }

        return "redirect:/product/" + productId;
    }


    @PreDestroy
    public void cleanUpActiveCarts() {
        cartService.deleteActiveCarts();

    }

    @GetMapping("/showCart")
    public String showCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        users currentUser = usersRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));

        Cart cart = cartService.findOrCreateCartByUserId(currentUser);
        List<Cart_Item> cartItems = cartItemService.findByCart(cart);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", cart.getTotalAmount());


        String username = authentication.getName();
        String logMessage = "Showing cart user: " + username;


        // Create a Log entity
        Log logEntry = new Log();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO");
        logEntry.setMessage(logMessage);

        // Save the Log entry to the database
        logService.save(logEntry,username);
        return "showCart";
    }
    @PostMapping("/removeFromCart")
    public String removeFromCart(@RequestParam("cartItemId") int cartItemId) {

        Cart_Item cartItem = cartItemService.findById(cartItemId);

        if (cartItem == null) {

            throw new IllegalArgumentException("Invalid cart item id: " + cartItemId);
        }


        double price = cartItem.getProduct().getPrice();
        int quantity = cartItem.getQuantity();


        cartItemService.deleteById(cartItemId);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        users currentUser = usersRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));

        Cart cart = cartService.findOrCreateCartByUserId(currentUser);


        double newTotalAmount = cart.getTotalAmount() - (price * quantity);
        cart.setTotalAmount(newTotalAmount);
        cartService.save(cart); // Sepeti kaydet


        String username = authentication.getName();
        String logMessage = "Removing product ("+ cartItem.getProduct().getName() +") and its quantity is "+quantity+" from cart user: " + username;


        // Create a Log entity
        Log logEntry = new Log();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO");
        logEntry.setMessage(logMessage);

        // Save the Log entry to the database
        logService.save(logEntry,username);
        return "showCart";}
    }
//
//    @PostMapping("/checkout")
//    public String checkout() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//
//        Cart cart = cartService.findOrCreateCartByUserId(currentPrincipalName);
//        cart.setStatus("completed");
//        cartService.save(cart);
//        return "redirect:/su-ve-icecek";
//    }



