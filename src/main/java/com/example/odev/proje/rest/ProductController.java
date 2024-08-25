package com.example.odev.proje.rest;

import com.example.odev.proje.entity.Product;
import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.service.LogService;
import com.example.odev.proje.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProductController {


    private final  ProductService productService;
private final LogService logService;
    @GetMapping("/product/{productId}")
    public String showProductDetails(@PathVariable("productId") int productId, Model model) {
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String logMessage = "Going to product ("+ product.getName() +") user: " + username;


        // Create a Log entity
        Log logEntry = new Log();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO");
        logEntry.setMessage(logMessage);

        // Save the Log entry to the database
        logService.save(logEntry,username);
        return "product-details";
    }
    @GetMapping("/searchSuggestions")
    public String searchSuggestions(@RequestParam("query") String query, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        model.addAttribute("query", query);

        String username = authentication.getName();
        String logMessage = "Going to searchSuggestions for"+query+ " user: " + username;


        // Create a Log entity
        Log logEntry = new Log();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO");
        logEntry.setMessage(logMessage);

        // Save the Log entry to the database
        logService.save(logEntry,username);
        return  "searchResults";
    }
}
