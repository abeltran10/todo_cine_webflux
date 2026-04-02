package com.todocine_webflux.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("categoria_premio")
public class CategoriaPremio {

    @Id
    private String id;

    private String premioId;

    private String titulo;

    private List<Categoria> categorias = new ArrayList<>();

    public CategoriaPremio() {
    }

    public CategoriaPremio(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPremioId() {
        return premioId;
    }

    public void setPremioId(String premioId) {
        this.premioId = premioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
}
