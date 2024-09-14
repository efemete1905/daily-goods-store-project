package com.example.odev.proje.rest;

import com.example.odev.proje.dao.usersRepository;
import com.example.odev.proje.entity.Product;
import com.example.odev.proje.entity.suveicecekler;
import com.example.odev.proje.entity.users;
import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.service.LogService;
import com.example.odev.proje.service.ProductService;
import com.example.odev.proje.service.suveiceceklerService;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class LoginController {

    private LogService logService;

    private ProductService productService;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private usersRepository usersrepository;


    @Autowired
    public LoginController(ProductService theproductService,  PasswordEncoder thepasswordEncoder, LogService theLogService) {
        this.productService = theproductService;
        this.passwordEncoder=thepasswordEncoder;
        this.logService = theLogService;
    }


    @GetMapping("/homepage")
    public String showHomePage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return "homepage";
    }
    @GetMapping("/login")
    public String showLoginnPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return "login";
    }

//    @PostMapping("/login")
//    public String showLoginPage() {
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        if (auth != null && auth.isAuthenticated() && !auth.getAuthorities().isEmpty()) {
//            return "welcome";
////        }
////        return "welcome";
//    }

//    @GetMapping("/login")
//    public String login() {
//        // Spring Security otomatik olarak bu işlemi halledecektir
//        return "redirect:/welcome";
//
//    }

    @GetMapping("/welcome")
    public String showWelcomePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || auth.getPrincipal() == null || auth.getPrincipal() instanceof String) {
//            return "redirect:/login"; // Oturum açmış bir kullanıcı yoksa veya principal bir String ise login sayfasına yönlendir
//        }

        String currentPrincipalName = auth.getName();
        model.addAttribute("username", currentPrincipalName);
        return "welcome"; // Kullanıcı oturumu açıksa "welcome" sayfasını döndürür
    }

   /* @GetMapping("/su-ve-icecek")
    public String showSuVeIcecekPage(Model model) {
        List<Product> products = productService.findByCategoryId(1L);// Buradaki categoriden su ve icecekleri aliyoruz
        model.addAttribute("products", products);
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String logMessage = " Goes to the su-ve-icecekler user:" + username;


        // Create a Log entity
        Log logEntry = new Log();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO");
        logEntry.setMessage(logMessage);

        // Save the Log entry to the database
        logService.save(logEntry,username);

        return "su-ve-icecek"; // su-ve-icecek.html sayfasını döndürür

    }*/

    @GetMapping("/category/{name}")
    public String getCategoryById(@RequestParam("id") Long id, @PathVariable("name") String name, Model model) {
        List<Product> products = productService.findByCategoryId(id);
        model.addAttribute("products", products);
        model.addAttribute("categoryName", name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String logMessage = "Goes to the " + name + " category, user: " + username;

        // Create a Log entity
        Log logEntry = new Log();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO");
        logEntry.setMessage(logMessage);

        // Save the Log entry to the database
        logService.save(logEntry, username);

        return "category"; // Return the view name dynamically based on the category name
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            String username = auth.getName();
            String logMessage = "LogOut successful for user: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("INFO");
            logEntry.setMessage(logMessage);

            logService.save(logEntry,username);
        }

        return "redirect:/homepage?logout"; // Redirect to login page with logout parameter
    }


    @GetMapping("/register")
    public String addEmployees(Model theModel) {
        users theEmployee = new users();
        theModel.addAttribute("employee", theEmployee);

        String username = null;
        String logMessage = " Goes to the register page user:";


        // Create a Log entity
        Log logEntry = new Log();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setLogLevel("INFO");
        logEntry.setMessage(logMessage);

        // Save the Log entry to the database
        logService.save(logEntry,username);

        return "user-form";


    }

    @PostMapping("/save")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") users theEmployee, BindingResult result){
        if (result.hasErrors()) {

            String username = null;
            String logMessage = "Some validation errors occurred while saving the user: ";


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("ERROR");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);
            return "user-form"; // Return to the form page if there are validation errors
        }
        try {
            BCryptPasswordEncoder bCryptpasswordEncoder = new BCryptPasswordEncoder();
            String encryptedPassword = bCryptpasswordEncoder.encode(theEmployee.getPassword());
           theEmployee.setPassword(encryptedPassword);
           theEmployee.setRole("ROLE_CUSTOMER");

            System.out.println("Saving user: " + theEmployee.getUsername()); // Log the username being saved
            usersrepository.save(theEmployee);
            String username = theEmployee.getUsername();
            String logMessage = "User saved successfully: " + username;


            // Create a Log entity
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("INFO");
            logEntry.setMessage(logMessage);

            // Save the Log entry to the database
            logService.save(logEntry,username);
            return "redirect:/login";
        } catch (Exception e) {
            Log logEntry = new Log();
            logEntry.setTimestamp(LocalDateTime.now());
            logEntry.setLogLevel("ERROR");
            logEntry.setMessage("Error saving user: " + e.getMessage());
            logService.save(logEntry,null);
            System.err.println("Error saving user: " + e.getMessage()); // Log any exception
            return "user-form"; // Return to the form page in case of error
        }
    }
    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor= new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
    }
}
