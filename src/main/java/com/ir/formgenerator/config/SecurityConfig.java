// package com.ir.formgenerator.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable()) // Disable CSRF if necessary
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/files/upload", "/files/list").permitAll() // Allow public access
//                 .anyRequest().authenticated()
//             );

//         return http.build();
//     }
// }
