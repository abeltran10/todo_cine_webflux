package com.todocine_webflux.entities;

public class Premio {

    private Long premioId;

    private String titulo;

    private Long categoriaId;

    private String categoria;

    private Integer anyo;

    public Premio() {
    }

   public Long getPremioId() {
        return premioId;
    }

    public void setPremioId(Long premioId) {
        this.premioId = premioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getAnyo() {
        return anyo;
    }

    public void setAnyo(Integer anyo) {
        this.anyo = anyo;
    }
}
