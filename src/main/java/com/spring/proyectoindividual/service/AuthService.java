package com.spring.proyectoindividual.service;

import com.spring.proyectoindividual.entity.Role;
import com.spring.proyectoindividual.entity.User;
import com.spring.proyectoindividual.exception.UserAlreadyExistsException;
import com.spring.proyectoindividual.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        // Verificar si el nombre de usuario ya existe
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("El nombre de usuario ya existe");
        }

        // Verificar si el correo electrónico ya existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El correo electrónico ya existe");
        }

        // Codificar la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar el usuario (el rol ya debería estar asignado en el controlador)
        System.out.println("Registrando usuario con rol: " + user.getRole());
        userRepository.save(user);

        // Si no se especificó un rol, establecer USER por defecto
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }


        userRepository.save(user);
    }
}