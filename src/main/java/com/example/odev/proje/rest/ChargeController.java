package com.example.odev.proje.rest;

import com.example.odev.proje.dao.usersRepository;
import com.example.odev.proje.entity.Cart;
import com.example.odev.proje.entity.ChargeRequest;
import com.example.odev.proje.entity.users;
import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.service.LogService;
import com.example.odev.proje.service.CartService;
import com.example.odev.proje.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.internet.MimeMessage;
import java.net.Authenticator;
import java.util.Properties;
import java.time.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
@Controller
@RequiredArgsConstructor
public class ChargeController {
    @Value("${EMAIL_PASSWORD}")
    String EMAIL_PASSWORD;


    private final StripeService paymentsService;

    private final CartService cartService;


    private final usersRepository usersRepository;

    private final LogService logService;
    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Model model)
            throws StripeException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        Charge charge = paymentsService.charge(chargeRequest);
        if ("succeeded".equals(charge.getStatus())) {
            // Kullanıcıyıdoğrulamayaptik
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            users currentUser = usersRepository.findByUsername(currentPrincipalName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentPrincipalName));

            Cart cart = cartService.findOrCreateCartByUserId(currentUser);
            cartService.updateCartStatusToShipped(cart);

            String username = authentication.getName();
            String logMessage = "Payment is correct, the cart is shipped user: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("INFO");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);

            String emailBody = "Siparisiniz alindi.";
            sendEmail(currentUser.getEmail(), "Kargo Bilgilendirme", emailBody);

        }
            model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "showingTheResult";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "showingTheResult";
    }





    // Add the email sending method in ChargeController
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
