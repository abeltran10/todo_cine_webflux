package com.todocine_webflux.entities;

public class Premio {

    private String premioId;

    private String titulo;

    private String categoria;

    private Integer anyo;

    public Premio() {
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
