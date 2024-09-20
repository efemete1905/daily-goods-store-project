package com.example.odev.proje.config;

import com.example.odev.proje.security.CustomAuthenticationFailureHandler;
import com.example.odev.proje.security.CustomAuthenticationSuccessHandler;
import com.example.odev.proje.security.CustomLogoutSecurityHandler;
import com.example.odev.proje.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private CustomLogoutSecurityHandler customLogoutSecurityHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/login", "/homepage", "/register","/oauth/**","/logout").permitAll()
                        .requestMatchers("/welcome").permitAll()
                        .requestMatchers("/shippedOrders").hasRole("COURIER")
                        .requestMatchers(HttpMethod.POST, "/save").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/login")
                .failureHandler(customAuthenticationFailureHandler)
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll()
        )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .failureHandler(new CustomAuthenticationFailureHandler())
                        .defaultSuccessUrl("/welcome")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutSecurityHandler)
                        .permitAll()
                )
                .sessionManagement(sessionManagement -> {
                    sessionManagement
                            .maximumSessions(1)
                            .expiredUrl("/login?expired")
                            .maxSessionsPreventsLogin(true);
                    sessionManagement
                            .sessionFixation(sessionFixation -> sessionFixation.newSession());
                })
                .headers(headers -> headers
                        .cacheControl(cacheControl -> cacheControl.disable())
                );

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth, DaoAuthenticationProvider authProvider) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
