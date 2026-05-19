package com.todocine_webflux.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MovieListaDTO {

    @NotNull
    private Long id;

    @JsonProperty("title")
    @NotBlank
    private String title;

    @JsonProperty("poster_path")
    @NotBlank
    private String posterPath;

    @JsonProperty("release_date")
    @NotBlank
    private String releaseDate;

    public MovieListaDTO() {
    }

    public MovieListaDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
