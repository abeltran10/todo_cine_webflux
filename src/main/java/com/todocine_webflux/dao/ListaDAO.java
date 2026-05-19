package com.todocine_webflux.dao;

import com.todocine_webflux.entities.Lista;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ListaDAO extends ReactiveMongoRepository<Lista, String> {

    Flux<Lista> findByUsername(String username, Pageable pageable);


    Mono<Long> countByUsername(String username);

    Mono<Long> countByPublica(String publica);

    Flux<Lista> findByPublica(String publica, Pageable pageable);
}
