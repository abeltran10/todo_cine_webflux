package com.todocine_webflux.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "listas")
public class Lista {

    @Id
    private String id; // ObjectId de Mongo se mapea automáticamente a String en Spring Data

    private String nombre;
    private String descripcion;

    @Indexed // Índice simple para buscar rápido las listas de un usuario concreto
    private String username;

    private String publica;

    private List<MovieLista> movies = new ArrayList<>();

    public Lista() {
    }

    public Lista(String id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublica() {
        return publica;
    }

    public void setPublica(String publica) {
        this.publica = publica;
    }

    public List<MovieLista> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieLista> movies) {
        this.movies = movies;
    }
}
