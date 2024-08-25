package com.example.odev.proje.rest;

import com.example.odev.proje.dao.CartRepository;
import com.example.odev.proje.dao.usersRepository;
import com.example.odev.proje.entity.CargoCartItem;
import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.PastCartItem;
import com.example.odev.proje.entity.users;
import com.example.odev.proje.service.CargoCartItemService;
import com.example.odev.proje.service.CartService;
import com.example.odev.proje.service.PastCartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.mail.internet.MimeMessage;
import java.net.Authenticator;
import java.util.Properties;
import java.time.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CargoController {
    @Value("${EMAIL_PASSWORD}")
    String EMAIL_PASSWORD;

    @Autowired
    private CartService cartService;


    private final PastCartItemService pastCartItemService;
    private final com.example.odev.proje.dao.usersRepository usersRepository;
    private final CartRepository cartRepository;
    private final CargoCartItemService cargoCartItemService;

    @Autowired
    public CargoController(PastCartItemService pastCartItemService, usersRepository usersRepository, CartRepository cartRepository, CargoCartItemService cargoCartItemService) {
        this.pastCartItemService = pastCartItemService;
        this.usersRepository = usersRepository;
        this.cartRepository = cartRepository;
        this.cargoCartItemService = cargoCartItemService;
    }

    @GetMapping("/shippedOrders")
    public String pastOrders(Model model) {
        // Kullanıcıyı doğrula
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//
//        users currentUser = usersRepository.findByUsername(currentPrincipalName)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));


        // Tamamlanmış sepetlere ait geçmiş sepet öğelerini al
        List<Cart> shippedCarts = cartRepository.findByStatus("shipped");

        // Geçmiş sepet öğeleri listesi
        List<CargoCartItem> combinedCargoCartItemsList = new ArrayList<>();

// hercarticinislem
        for (Cart shippedCart : shippedCarts) {
            List<CargoCartItem> cargoCartItems = cargoCartItemService.findByCart(shippedCart);
            combinedCargoCartItemsList.addAll(cargoCartItems);
        }

        model.addAttribute("completedCarts", shippedCarts);
        model.addAttribute("pastCartItemsList", combinedCargoCartItemsList);

        return "shippedOrders";
    }

    @PostMapping("/completeShippedOrders")
    public String completeShippedOrders(@RequestParam("cartId") Long cartId) {
        Cart cart = cartRepository.findById(cartId);
         users user1=cart.getUser();

            cartService.updateCartStatusToCompleted(cart);


        String emailBody = "Siparisiniz teslim edildi.";
        sendEmail(user1.getEmail(), "Kargo Bilgilendirme", emailBody);


        return "redirect:/shippedOrders";
    }




    private void sendEmail(String to, String subject, String body) {
        String SMTP_HOST = "smtp.gmail.com";
        String SMTP_PORT = "587";
        String EMAIL_FROM = "meteefeeren@gmail.com";


        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.localhost", "smtp.gmail.com");

        javax.mail.Authenticator authenticator = new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        };


        Session session = Session.getInstance(properties,authenticator);
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}

