package com.todocine_webflux.dao;

import com.todocine_webflux.entities.Premio;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PremioDAO extends ReactiveMongoRepository<Premio, String> {
}
