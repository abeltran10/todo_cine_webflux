package com.todocine_webflux.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies")
public class Movie {

    @Id
    private String id;

    private String originalTitle;

    private String title;

    private String posterPath;

    private String overview;

    private String releaseDate;

    private Double popularity;

    private Integer voteCount;

    private Double voteAverage;

    private String originalLanguage;

    private Integer totalVotosTC;

    private Double votosMediaTC;

    public Movie() {
    }

    public Movie(String id) {
        this.id = id;
    }

    public Movie(String id, String originalTitle, String title, String posterPath, String overview, String releaseDate,
                 Double popularity, Integer voteCount, Double voteAverage, String originalLanguage,
                 Integer totalVotosTC, Double votosMediaTC) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.originalLanguage = originalLanguage;
        this.totalVotosTC = totalVotosTC;
        this.votosMediaTC = votosMediaTC;
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

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
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
