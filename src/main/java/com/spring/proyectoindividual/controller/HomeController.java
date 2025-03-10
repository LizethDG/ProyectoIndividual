package com.spring.proyectoindividual.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username = authentication.getName();
            String email = null;

            // Si el usuario viene de Google OAuth2, obtener datos de OAuth2User
            if (principal instanceof OAuth2User oauth2User) {
                username = oauth2User.getAttribute("name");
                email = oauth2User.getAttribute("email");
            }

            // Agregar al modelo
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }
        return "home";
    }
}
