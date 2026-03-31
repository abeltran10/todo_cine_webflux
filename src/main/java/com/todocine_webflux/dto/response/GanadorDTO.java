package com.todocine_webflux.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.todocine_webflux.entities.Categoria;
import com.todocine_webflux.entities.Ganador;
import com.todocine_webflux.entities.Movie;
import com.todocine_webflux.entities.Premio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GanadorDTO {

    @JsonProperty("premioId")
    @NotNull
    private Long premioId;

    @JsonProperty("premio")
    private String premio;

    @JsonProperty("categoriaId")
    @NotNull
    private Long categoriaId;

    @JsonProperty("categoria")
    private String categoria;

    @JsonProperty("anyo")
    @NotNull
    private Integer anyo;

    @JsonProperty("movieId")
    @NotNull
    private Long movieId;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("title")
    private String title;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;


    public GanadorDTO() {
    }

    public Long getPremioId() {
        return premioId;
    }

    public void setPremioId(Long premioId) {
        this.premioId = premioId;
    }

    public String getPremio() {
        return premio;
    }

    public void setPremio(String premio) {
        this.premio = premio;
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

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}

