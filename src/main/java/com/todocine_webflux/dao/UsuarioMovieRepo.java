package com.todocine_webflux.dao;

import com.todocine_webflux.entities.UsuarioMovie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UsuarioMovieRepo {

    Flux<UsuarioMovie> findWithFilters(String userId, Map<String, String> filters, String orderBy, int offset, int limit);

    Mono<Long> countWithFilters(String userId, Map<String, String> filters);
}
