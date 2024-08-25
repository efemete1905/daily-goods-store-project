package com.example.odev.proje.security;

import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class CustomLogoutSecurityHandler implements LogoutSuccessHandler {
    private final LogService logService;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null && authentication.getName() != null) {
            String username = authentication.getName();
            String logMessage = "LogOut successful for user: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("INFO");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);

        }
        response.sendRedirect("/homepage");
    }
}

