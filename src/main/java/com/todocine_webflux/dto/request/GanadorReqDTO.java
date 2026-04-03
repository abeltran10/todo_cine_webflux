package com.todocine_webflux.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GanadorReqDTO {

    @JsonProperty("premioId")
    @NotNull
    private Long premioId;

    @JsonProperty("categoriaId")
    @NotNull
    private Long categoriaId;

    @JsonProperty("anyo")
    @NotNull
    private Integer anyo;

    @JsonProperty("movieId")
    @NotNull
    private Long movieId;

    public GanadorReqDTO() {
    }

    public GanadorReqDTO(Long premioId, Long categoriaId, Integer anyo, Long movieId) {
        this.premioId = premioId;
        this.categoriaId = categoriaId;
        this.anyo = anyo;
        this.movieId = movieId;
    }

    public Long getPremioId() {
        return premioId;
    }

    public void setPremioId(Long premioId) {
        this.premioId = premioId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Integer getAnyo() {
        return anyo;
    }

    public void setAnyo(Integer anyo) {
        this.anyo = anyo;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}

