package com.fatec.livraria.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desativa proteção CSRF (caso use POST sem token)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permite todas as requisições
            )
            .formLogin(login -> login.disable()) // Desativa tela de login
            .httpBasic(basic -> basic.disable()); // Desativa autenticação básica

        return http.build();
    }
}

