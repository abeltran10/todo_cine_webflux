package com.todocine_webflux.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface TMDBService {

    Mono<Map<String, Object>> getMovieById(String id);

    Mono<Map<String, Object>> getMoviesByName(String name, Integer pagina);

    Mono<Map<String, Object>> getMoviesPlayingNow(String country, Integer pagina);

    Mono<Map<String, Object>> getVideosByMovieId(String movieId);
}
