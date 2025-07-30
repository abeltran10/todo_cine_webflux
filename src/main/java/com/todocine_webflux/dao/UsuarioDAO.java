package com.todocine_webflux.dao;

import com.todocine_webflux.entities.Usuario;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UsuarioDAO extends ReactiveMongoRepository<Usuario, String> {
    Mono<Usuario> findByUsername(String username);
}
