package com.todocine_webflux.dao;

import com.todocine_webflux.entities.CategoriaPremio;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CategoriaPremioDAO extends ReactiveMongoRepository<CategoriaPremio, String>  {

    Mono<CategoriaPremio> findByPremioId(Long premioId);


}
