package com.todocine_webflux.dao;

import com.todocine_webflux.entities.Ganador;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GanadorRepo {

    Flux<Ganador> findByPremioIdAndAnyo(String premioId, Integer anyo, int offset, int limit);

    Mono<Long> countByPremioIdAndAnyo(String premioId, Integer anyo);
}
