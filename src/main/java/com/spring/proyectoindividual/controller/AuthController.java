package com.spring.proyectoindividual.controller;

import com.spring.proyectoindividual.entity.Role;
import com.spring.proyectoindividual.entity.User;
import com.spring.proyectoindividual.exception.UserAlreadyExistsException;
import com.spring.proyectoindividual.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    @Value("${admin.code}") // Inyectar el código de administrador desde application.properties
    private String adminCode;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // Agregar un nuevo usuario al modelo
        return "register"; // Retornar la vista del formulario de registro
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam(required = false) String adminCode,
                               RedirectAttributes redirectAttributes) {
        try {
            // Logs para depuración
            System.out.println("Código de administrador configurado: " + this.adminCode);
            System.out.println("Código de administrador ingresado: " + adminCode);

            // Verificar si el código de administrador es correcto
            if (this.adminCode.equals(adminCode)) {
                System.out.println("Asignando rol de ADMIN");
                user.setRole(Role.ADMIN); // Asignar rol de ADMIN
            } else {
                System.out.println("Asignando rol de USER");
                user.setRole(Role.USER); // Asignar rol de USER por defecto
            }

            // Registrar el usuario
            authService.registerUser(user);
            redirectAttributes.addFlashAttribute("message", "Registro exitoso. Por favor inicia sesión.");
            return "redirect:/login"; // Redirigir al formulario de login después del registro
        } catch (UserAlreadyExistsException e) {
            // Manejar excepción si el usuario ya existe
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register"; // Redirigir al formulario de registro si hay un error
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Retornar la vista del formulario de login
    }
}