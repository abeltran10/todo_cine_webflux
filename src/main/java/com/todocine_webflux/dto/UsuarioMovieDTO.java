package com.todocine_webflux.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioMovieDTO {

    @JsonProperty("usuarioId")
    @NotNull
    private String usuarioId;

    @JsonProperty("movieId")
    @NotBlank
    private String movieId;

    @JsonProperty("favoritos")
    @NotNull
    private Boolean favoritos;

    @JsonProperty("vista")
    @NotNull
    private Boolean vista;

    @JsonProperty("voto")
    private Double voto;

    public UsuarioMovieDTO() {
    }

    public UsuarioMovieDTO(String usuarioId, String movieId) {
        this.usuarioId = usuarioId;
        this.movieId = movieId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Boolean getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Boolean favoritos) {
        this.favoritos = favoritos;
    }

    public Boolean getVista() {
        return vista;
    }

    public void setVista(Boolean vista) {
        this.vista = vista;
    }

    public Double getVoto() {
        return voto;
    }

    public void setVoto(Double voto) {
        this.voto = voto;
    }
}
