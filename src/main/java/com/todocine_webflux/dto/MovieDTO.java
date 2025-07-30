package com.todocine_webflux.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {


    @JsonProperty("id")
    @NotBlank
    private String id;

    @JsonProperty("original_title")
    @NotBlank
    private String originalTitle;

    @JsonProperty("title")
    @NotBlank
    private String title;

    @JsonProperty("poster_path")
    @NotBlank
    private String posterPath;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("release_date")
    @NotBlank
    private String releaseDate;

    @JsonProperty("popularity")
    @NotNull
    private Double popularity;

    @JsonProperty("vote_count")
    @NotNull
    private Integer voteCount;

    @JsonProperty("vote_average")
    @NotNull
    private Double voteAverage;

    @JsonProperty("genres")
    @NotNull
    private List<GenreDTO> genreDTOS;

    @JsonProperty("original_language")
    @NotBlank
    private String originalLanguage;

    @JsonProperty("videos")
    private List<VideoDTO> videoDTOS;

    @JsonProperty("total_votos_TC")
    @NotNull
    private Integer totalVotosTC;

    @JsonProperty("votos_media_TC")
    @NotNull
    private Double votosMediaTC;

    public MovieDTO() {
    }

    public MovieDTO(String id) {
        this.id = id;
    }

    public MovieDTO(String id, String originalTitle, String title, String posterPath, String overview, String releaseDate,
                    Double popularity, Integer voteCount, Double voteAverage, List<GenreDTO> genreDTOS, String originalLanguage,
                    List<VideoDTO> videoDTOS, Integer totalVotosTC, Double votosMediaTC) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.genreDTOS = genreDTOS;
        this.originalLanguage = originalLanguage;
        this.videoDTOS = videoDTOS;
        this.totalVotosTC = totalVotosTC;
        this.votosMediaTC = votosMediaTC;
    }

    public MovieDTO(String id, String originalTitle, String posterPath) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
    }

    public MovieDTO(String id, String originalTitle) {
        this.id = id;
        this.originalTitle = originalTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public List<GenreDTO> getGenres() {
        return genreDTOS;
    }

    public void setGenres(List<GenreDTO> genreDTOS) {
        this.genreDTOS = genreDTOS;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public List<VideoDTO> getVideos() {
        return videoDTOS;
    }

    public void setVideos(List<VideoDTO> videoDTOS) {
        this.videoDTOS = videoDTOS;
    }

    public Integer getTotalVotosTC() {
        return totalVotosTC;
    }

    public void setTotalVotosTC(Integer totalVotosTC) {
        this.totalVotosTC = totalVotosTC;
    }

    public Double getVotosMediaTC() {
        return votosMediaTC;
    }

    public void setVotosMediaTC(Double votosMediaTC) {
        this.votosMediaTC = votosMediaTC;
    }

}
