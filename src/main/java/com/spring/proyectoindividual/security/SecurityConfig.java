package com.spring.proyectoindividual.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF (opcional)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll() // Permitir acceso público
                        .anyRequest().authenticated() // Requerir autenticación para cualquier otra ruta
                )
                .formLogin(form -> form
                        .loginPage("/login") // Página de login personalizada
                        .defaultSuccessUrl("/home", true) // Redirigir a /home después del login exitoso
                        .failureUrl("/login?error=true") // Redirigir en caso de error
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true") // Redirigir después del logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}