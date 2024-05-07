//package com.learningspringboot.samah.employees.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.config.authentication.PasswordEncoderParser;
//import org.springframework.security.config.authentication.PasswordEncoderParser;
//import org.springframework.security.core.userdetails.MyUser;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails myUser = MyUser.builder()
//                .username("myUser")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//        UserDetails admin = MyUser.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(myUser, admin);
//    }
//
//    @Bean
//    public PasswordEncoderParser passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    //Other beans
//}
