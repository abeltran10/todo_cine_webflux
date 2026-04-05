package com.todocine_webflux.dao;


import com.todocine_webflux.dto.response.GanadorDTO;
import com.todocine_webflux.dto.response.PremioDTO;
import com.todocine_webflux.entities.Movie;
import com.todocine_webflux.entities.Premio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieDAO extends ReactiveMongoRepository<Movie, Long> {
    Mono<Movie> findMovieById(Long id);

    @Aggregation(pipeline = {
            // 1. Filtro rápido
            "{ '$match': { 'premios': { '$elemMatch': { 'premioId': ?0, 'anyo': ?1 } } } }",

            // 2. Expandir premios
            "{ '$unwind': '$premios' }",

            // 3. Filtrar el premio exacto
            "{ '$match': { 'premios.premioId': ?0, 'premios.anyo': ?1 } }",

            // 4. Proyección: La clave es que el nombre de la izquierda
            // debe ser IGUAL al nombre de la variable en tu DTO.
            "{ '$project': { " +
                    "'premioId': '$premios.premioId', " +
                    "'premio': '$premios.titulo', " +
                    "'categoriaId': '$premios.categoriaId', " +
                    "'categoria': '$premios.categoria', " +
                    "'anyo': '$premios.anyo', " +

                    // Si en tu DTO la variable se llama 'movieId', aquí va 'movieId'
                    "'movieId': '$id', " +

                    // Si tu variable en Java se llama 'originalTitle', usa 'originalTitle' aquí:
                    "'originalTitle': '$original_title', " +
                    "'title': '$title', " +
                    "'posterPath': '$poster_path', " +
                    "'overview': '$overview', " +
                    "'releaseDate': '$release_date' " +
                    "} }",

            "{ '$skip': ?2 }",
            "{ '$limit': ?3 }"
    })
    Flux<GanadorDTO> findGanadoresByPremioAndAnyo(Long premioId, Integer anyo, long skip, int limit);

    // El conteo también debe hacerse sobre los premios "unwinded" para ser exacto
    @Aggregation(pipeline = {
            "{ '$match': { 'premios': { '$elemMatch': { 'premioId': ?0, 'anyo': ?1 } } } }",
            "{ '$unwind': '$premios' }",
            "{ '$match': { 'premios.premioId': ?0, 'premios.anyo': ?1 } }",
            "{ '$count': 'total' }"
    })
    Mono<Long> countGanadoresByPremioAndAnyo(Long premioId, Integer anyo);
}
