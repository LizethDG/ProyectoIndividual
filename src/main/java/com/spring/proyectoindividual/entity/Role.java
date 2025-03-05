package com.spring.proyectoindividual.entity;

public enum Role {
    USER("Usuario"),
    ADMIN("Administrador");

    private String description;

    Role(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
