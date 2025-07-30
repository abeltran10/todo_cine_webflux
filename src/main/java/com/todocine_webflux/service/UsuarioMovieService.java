package com.todocine_webflux.service;

import com.todocine_webflux.dto.MovieDetailDTO;
import com.todocine_webflux.dto.UsuarioMovieDTO;
import com.todocine_webflux.utils.Paginator;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UsuarioMovieService {

    Mono<Paginator<MovieDetailDTO>> getUsuarioMovies(String userId, Map<String, String> filters, String orderBy, Integer page);

    Mono<MovieDetailDTO> updateUsuarioMovie(String userId, String movieId, UsuarioMovieDTO dto);
}
