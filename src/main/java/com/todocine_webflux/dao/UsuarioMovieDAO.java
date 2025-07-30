package com.todocine_webflux.dao;


import com.todocine_webflux.entities.Usuario;
import com.todocine_webflux.entities.UsuarioMovie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UsuarioMovieDAO extends ReactiveMongoRepository<UsuarioMovie, String>  {

    Mono<UsuarioMovie> findByUsuarioIdAndMovieId(String usuarioId, String movieId);
}
