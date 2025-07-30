package com.todocine_webflux.dao;


import com.todocine_webflux.entities.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieDAO extends ReactiveMongoRepository<Movie, String> {
}
