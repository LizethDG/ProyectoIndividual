package com.spring.proyectoindividual.service;

import com.spring.proyectoindividual.entity.Usuario;
import com.spring.proyectoindividual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombre(nombre);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + nombre);
        }
        return new CustomUserDetails(usuario);
    }
}
