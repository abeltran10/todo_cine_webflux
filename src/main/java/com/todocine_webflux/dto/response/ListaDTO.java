package com.todocine_webflux.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListaDTO {

    @NotNull
    private String id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String descripcion;

    private List<MovieListaDTO> movies = new ArrayList<>();

    @NotBlank
    private String username;

    @NotNull
    private Boolean publica;

    public ListaDTO() {
    }

    public ListaDTO(String id) {
        this.id = id;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<MovieListaDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieListaDTO> movies) {
        this.movies = movies;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getPublica() {
        return publica;
    }

    public void setPublica(Boolean publica) {
        this.publica = publica;
    }
}
