package com.todocine_webflux.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDAO extends ReactiveMongoRepository<Categoria, String>  {
}
