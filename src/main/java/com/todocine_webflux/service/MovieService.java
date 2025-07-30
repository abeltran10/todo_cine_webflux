package com.todocine_webflux.service;

import com.todocine_webflux.dto.MovieDTO;
import com.todocine_webflux.dto.MovieDetailDTO;
import com.todocine_webflux.utils.Paginator;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface MovieService {

    Mono<MovieDetailDTO> getMovieDetailById(String id);

    Mono<Paginator<MovieDTO>> getMovies(Map<String, String> filters, Integer pagina);
}
