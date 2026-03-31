package com.todocine_webflux.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriaDTO {

    @NotNull
    private String id;


    @JsonProperty("nombre")
    @NotBlank
    private String nombre;

    public CategoriaDTO() {
    }

    public CategoriaDTO(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}

