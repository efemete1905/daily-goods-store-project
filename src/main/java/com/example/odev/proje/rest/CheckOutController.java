package com.example.odev.proje.rest;

import com.example.odev.proje.dao.usersRepository;
import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.ChargeRequest;
import com.example.odev.proje.entity.users;
import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.service.LogService;
import com.example.odev.proje.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class CheckOutController {



    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;
    private final CartService cartService;
    private final usersRepository usersRepository;
    private final LogService logService;

//    @Autowired
//    public CheckOutController(CartService cartService, usersRepository theusersRepository) {
//        this.cartService = cartService;
//        this.usersRepository = theusersRepository;
//    }
    @RequestMapping("/checkout")
    public String checkout(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        users currentUser = usersRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));

        // Kullanıcıyaaitsepetvetutarhesaplama
        Cart cart = cartService.findOrCreateCartByUserId(currentUser);
        double totalAmount = cart.getTotalAmount();
        if (totalAmount < 50) {
            model.addAttribute("errorMessage", "Ödeme yapabilmek için sepetinizin toplam tutarı en az 50 TL olmalıdır.");


            String username = authentication.getName();
            String logMessage = "The total amount of the cart is less than 50 TL user: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("ERROR");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);

            return "showCart"; // Kullanıcının sepet sayfasına geri dönmesini sağlar
        }



        model.addAttribute("amount", (int) (totalAmount * 100)); // Stripe ödeme işlemi için toplam tutarı cent cinsinden ekle
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.EUR);


        String username = authentication.getName();
        String logMessage ="User is going to the Credit Card form user: " + username;


        // Create a Log entity
        Log logEntry = new Log();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO");
        logEntry.setMessage(logMessage);

        // Save the Log entry to the database
        logService.save(logEntry,username);

        return "StripeCardForm";

    }
}