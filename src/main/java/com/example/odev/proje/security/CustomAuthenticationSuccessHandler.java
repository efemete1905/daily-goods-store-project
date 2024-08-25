package com.example.odev.proje.security;


import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.service.LogService;
import jakarta.persistence.Access;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;



@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
@Autowired
private LogService logService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());


        if (roles.contains("ROLE_COURIER")) {
            String username = authentication.getName();
            String logMessage = "Authentication successful for user: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("INFO");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);
            response.sendRedirect("/shippedOrders");
        } else if (roles.contains("ROLE_CUSTOMER")) {
            String username = authentication.getName();
            String logMessage = "Authentication successful for user: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("INFO");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);
            response.sendRedirect("/welcome");
        } else {
            response.sendRedirect("/login");
        }
    }
}