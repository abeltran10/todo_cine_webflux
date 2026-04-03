package com.todocine_webflux.entities;

import org.springframework.data.mongodb.core.mapping.Field;

public class Categoria {

    @Field("id")
    private Long id;

    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
