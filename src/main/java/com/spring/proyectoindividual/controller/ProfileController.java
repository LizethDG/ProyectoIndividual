package com.spring.proyectoindividual.controller;

import com.spring.proyectoindividual.entity.User;
import com.spring.proyectoindividual.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/perfil")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Vista para editar el perfil
    @GetMapping
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get());
            return "perfil";
        } else {
            model.addAttribute("error", "Usuario no encontrado");
            return "error";
        }
    }

    // Manejar la actualización del perfil
    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("username") String username,
                                @RequestParam("email") String email,
                                @RequestParam("profilePicture") MultipartFile profilePicture,
                                Model model) throws IOException {
        String userEmail = userDetails.getUsername();

        System.out.println("Método updateProfile llamado para usuario: " + userEmail);

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setUsername(username);
        user.setEmail(email);

        if (!profilePicture.isEmpty()) {
            // Eliminar imagen anterior si existe
            if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                Path oldFilePath = Paths.get("src/main/resources/static/uploads/" + user.getProfilePicture());
                try {
                    Files.deleteIfExists(oldFilePath);
                    System.out.println("Imagen anterior eliminada: " + oldFilePath);
                } catch (IOException e) {
                    System.out.println("No se pudo eliminar la imagen anterior: " + e.getMessage());
                }
            }

            String fileName = System.currentTimeMillis() + "_" + profilePicture.getOriginalFilename();
            String uploadDir = "src/main/resources/static/uploads/";

            // Usar Files.createDirectories para crear toda la estructura de directorios
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            try {
                // Guardar el archivo usando FileOutputStream en lugar de Files.copy
                try (FileOutputStream fos = new FileOutputStream(uploadDir + fileName)) {
                    fos.write(profilePicture.getBytes());
                }

                System.out.println("Archivo guardado en: " + uploadDir + fileName);

                // Verificar si el archivo existe después de guardarlo
                Path filePath = uploadPath.resolve(fileName);
                if (Files.exists(filePath)) {
                    System.out.println("Archivo verificado en el sistema: " + filePath);
                } else {
                    System.out.println("ERROR: El archivo no existe después de guardarlo: " + filePath);
                }

                user.setProfilePicture(fileName);
                System.out.println("Nombre de archivo guardado en BD: " + fileName);
            } catch (Exception e) {
                System.out.println("Error al guardar el archivo: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("error", "No se pudo guardar la imagen: " + e.getMessage());
            }
        }

        userRepository.save(user);

        // Verificar el usuario después de guardar
        User savedUser = userRepository.findByEmail(userEmail).orElse(null);
        if (savedUser != null) {
            System.out.println("Perfil actualizado. Nombre de archivo en BD después de guardar: " + savedUser.getProfilePicture());
        }

        // Pasar el usuario directamente desde la base de datos para asegurar que tiene datos actualizados
        model.addAttribute("user", savedUser != null ? savedUser : user);
        model.addAttribute("message", "Perfil actualizado con éxito.");
        return "perfil";
    }

    // Actualización de contraseña
    @PostMapping("/update-password")
    public String updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("error", "La contraseña actual es incorrecta.");
            return "perfil";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Las nuevas contraseñas no coinciden.");
            return "perfil";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("message", "Contraseña actualizada con éxito.");
        return "perfil";
    }
}