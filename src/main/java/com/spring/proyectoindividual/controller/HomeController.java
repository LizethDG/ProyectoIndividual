package com.spring.proyectoindividual.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        // Verificar si el usuario está autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            // Obtener el nombre de usuario
            String username = authentication.getName();
            model.addAttribute("username", username);

            // Verificar si el usuario tiene el rol de ADMIN
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        }

        // Devolver la vista de home
        return "home";
    }
}