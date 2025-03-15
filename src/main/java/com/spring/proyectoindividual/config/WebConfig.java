package com.spring.proyectoindividual.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapear la ruta física absoluta del sistema de archivos
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
        Path uploadPath = Paths.get(uploadDir);
        String absolutePath = uploadPath.toFile().getAbsolutePath();

        System.out.println("Configurando ruta de recursos: file:" + absolutePath + "/");

        // Configuración para la carpeta de uploads con ruta absoluta
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolutePath + "/");

        // Configuración para otros recursos estáticos (mantener si es necesario)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}