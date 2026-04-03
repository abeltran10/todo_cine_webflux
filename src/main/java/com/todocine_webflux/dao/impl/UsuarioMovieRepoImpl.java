package com.todocine_webflux.dao.impl;


import com.todocine_webflux.config.Constants;
import com.todocine_webflux.dao.UsuarioMovieRepo;
import com.todocine_webflux.entities.UsuarioMovie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.todocine_webflux.config.Constants.*;

@Repository
public class UsuarioMovieRepoImpl implements UsuarioMovieRepo {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;


    @Override
    public Flux<UsuarioMovie> findWithFilters(String userId, Map<String, String> filters, String orderBy, int offset, int limit) {
        List<AggregationOperation> operations = new ArrayList<>();

        Criteria criteria = Criteria.where("usuarioId").is(userId)
                                .and("favoritos").is("S");

        if (!filters.get(VISTA_FILTER).isBlank()) {
            criteria = criteria.and("vista").is(filters.get(VISTA_FILTER).toUpperCase());
        }
        if (!filters.get(VOTADA_FILTER).isBlank()) {
            if (filters.get(VOTADA_FILTER).equalsIgnoreCase("S")) {
                criteria = criteria.and("voto").ne(null);
            } else if (filters.get(VOTADA_FILTER).equalsIgnoreCase("N")) {
                criteria = criteria.and("voto").is(null);
            }
        }

        operations.add(Aggregation.match(criteria));

        // Join with movies collection
        operations.add(Aggregation.lookup("movies", "movieId", "id", "movie"));

        // Flatten the movie array
        operations.add(Aggregation.unwind("movie", true));
        // Sort
        if (ORDER_TITULO.equalsIgnoreCase(orderBy)) {
            operations.add(Aggregation.sort(Sort.by(Sort.Direction.ASC, "movie.title")));
        } else if (ORDER_ANYO.equalsIgnoreCase(orderBy)) {
            operations.add(Aggregation.sort(Sort.by(Sort.Direction.ASC, "movie.releaseDate")));
        }

        // Pagination
        operations.add(Aggregation.skip(offset));
        operations.add(Aggregation.limit(limit));

        Aggregation aggregation = Aggregation.newAggregation(operations);

        return mongoTemplate.aggregate(aggregation, "usuario_movie", UsuarioMovie.class);
    }

    @Override
    public Mono<Long> countWithFilters(String userId, Map<String, String> filters) {
        Criteria criteria = Criteria.where("usuarioId").is(userId).and("favoritos").is("S");

        if (!filters.get(VISTA_FILTER).isBlank()) {
            criteria = criteria.and("vista").is(filters.get(VISTA_FILTER).toUpperCase());
        }
        if (!filters.get(VOTADA_FILTER).isBlank()) {
            if (filters.get(VOTADA_FILTER).equalsIgnoreCase("S")) {
                criteria = criteria.and("voto").ne(null);
            } else if (filters.get(VOTADA_FILTER).equalsIgnoreCase("N")) {
                criteria = criteria.and("voto").is(null);
            }
        }

        Query query = new Query(criteria);
        return mongoTemplate.count(query, UsuarioMovie.class);
    }
}
