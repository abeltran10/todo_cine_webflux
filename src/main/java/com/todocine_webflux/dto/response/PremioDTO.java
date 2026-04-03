package com.todocine_webflux.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremioDTO {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("titulo")
    @NotBlank
    private String titulo;

    @JsonProperty("anyos")
    private List<Integer> anyos;

    public PremioDTO() {
    }

    public PremioDTO(Long id) {
        this.id = id;
    }

    public PremioDTO(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public PremioDTO(Long id, String titulo, List<Integer> anyos) {
        this.id = id;
        this.titulo = titulo;
        this.anyos = anyos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Integer> getAnyos() {
        return anyos;
    }

    public void setAnyos(List<Integer> anyos) {
        this.anyos = anyos;
    }
}

