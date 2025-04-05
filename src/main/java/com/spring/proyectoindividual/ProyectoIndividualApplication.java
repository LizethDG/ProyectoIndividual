package com.spring.proyectoindividual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.spring.proyectoindividual.config",
    "com.spring.proyectoindividual.controller",
    "com.spring.proyectoindividual.entity",
    "com.spring.proyectoindividual.repository",
    "com.spring.proyectoindividual.service",
    "com.spring.proyectoindividual.SistemaAutenticacion"
})
@EnableJpaRepositories(basePackages = "com.spring.proyectoindividual.repository")
@EntityScan(basePackages = "com.spring.proyectoindividual.entity")
public class ProyectoIndividualApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProyectoIndividualApplication.class, args);
    }
}
