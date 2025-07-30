package com.todocine_webflux.dao;

import com.todocine_webflux.entities.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDAO extends ReactiveMongoRepository<Categoria, String>  {
}
