package com.example.odev.proje.rest;

import com.example.odev.proje.dao.CartRepository;
import com.example.odev.proje.dao.usersRepository;
import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.PastCartItem;
import com.example.odev.proje.entity.users;
import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.service.LogService;
import com.example.odev.proje.service.PastCartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PastCartController {

    private final PastCartItemService pastCartItemService;
    private final usersRepository usersRepository;
    private final CartRepository cartRepository;
    private final LogService logService;

//    @Autowired
//    public PastCartController(PastCartItemService pastCartItemService, usersRepository usersRepository, CartRepository cartRepository) {
//        this.pastCartItemService = pastCartItemService;
//        this.usersRepository = usersRepository;
//        this.cartRepository = cartRepository;
//    }

    @RequestMapping("/pastOrders")
    public String pastOrders(Model model) {
        // Kullanıcıyı doğrula
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        users currentUser = usersRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));



        // Tamamlanmış sepetlere ait geçmiş sepet öğelerini al
        List<Cart> completedCarts = cartRepository.findByUserIdAndStatus(currentUser.getId(), "completed");

        // Geçmiş sepet öğeleri listesi
        List<PastCartItem> combinedPastCartItemsList = new ArrayList<>();

// hercarticinislem
        for (Cart completedCart : completedCarts) {
            List<PastCartItem> pastCartItems = pastCartItemService.findByCart(completedCart);
            combinedPastCartItemsList.addAll(pastCartItems);
        }

        model.addAttribute("completedCarts", completedCarts);
        model.addAttribute("pastCartItemsList", combinedPastCartItemsList);

        String username = authentication.getName();
        String logMessage = "Checking past orders user: " + username;


        // Create a Log entity
        Log logEntry = new Log();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO");
        logEntry.setMessage(logMessage);

        // Save the Log entry to the database
        logService.save(logEntry,username);

        return "pastOrders";
    }
}
