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

public interface MovieDAO extends ReactiveMongoRepository<Movie, String> {
    @Aggregation(pipeline = {
            "{ '$unwind': '$premios' }",
            "{ '$group': { " +
                    "'_id': '$premios.premioId', " +
                    "'titulo': { '$first': '$premios.titulo' }, " +
                    "'anyos': { '$addToSet': '$premios.anyo' } " + // Crea la lista de años únicos
                    "} }",
            "{ '$project': { 'premioId': '$_id', 'titulo': 1, 'anyos': 1, '_id': 0 } }"
    })
    Flux<PremioDTO> findDistinctPremiosConAnyos(); // Retorna directamente el DTO

    @Aggregation(pipeline = {
            // 1. Filtramos las películas que contienen ese premio para no procesar toda la DB
            "{ '$match': { 'premios.premioId': ?0 } }",

            // 2. Desglosamos el array
            "{ '$unwind': '$premios' }",

            // 3. Volvemos a filtrar (tras el unwind) para quedarnos solo con el premio buscado
            "{ '$match': { 'premios.premioId': ?0 } }",

            // 4. Agrupamos para obtener el título y la lista de años únicos
            "{ '$group': { " +
                    "'_id': '$premios.premioId', " +
                    "'titulo': { '$first': '$premios.titulo' }, " +
                    "'anyos': { '$addToSet': '$premios.anyo' } " +
                    "} }",

            // 5. Proyectamos al formato final
            "{ '$project': { " +
                    "'premioId': '$_id', " +
                    "'titulo': 1, " +
                    "'anyos': 1, " +
                    "'_id': 0 " +
                    "} }"
    })
    Mono<PremioDTO> findPremioById(String premioId);

    @Aggregation(pipeline = {
            // 1. Filtrado inicial por índice (muy rápido)
            "{ '$match': { 'premios': { '$elemMatch': { 'premioId': ?0, 'anyo': ?1 } } } }",

            // 2. Desglosar el array para obtener un resultado por cada categoría
            "{ '$unwind': '$premios' }",

            // 3. Filtrar los premios específicos que coinciden con ID y Año
            "{ '$match': { 'premios.premioId': ?0, 'premios.anyo': ?1 } }",

            // 4. Proyección exacta al GanadorDTO
            "{ '$project': { " +
                    "'premioId': '$premios.premioId', " +
                    "'premio': '$premios.titulo', " +      // Mapea a 'premio' en el DTO
                    "'categoriaId': '$premios.categoriaId', " + // Asegúrate que este campo existe en tu Mongo
                    "'categoria': '$premios.categoria', " +
                    "'anyo': '$premios.anyo', " +
                    "'movieId': '$_id', " +                // El ID de la película
                    "'original_title': '$original_title', " +
                    "'title': '$title', " +
                    "'poster_path': '$poster_path', " +
                    "'overview': '$overview', " +
                    "'release_date': '$release_date', " +
                    "'_id': 0 " +                          // Importante: excluir _id para evitar conflictos
                    "} }",

            // 5. Paginación
            "{ '$skip': ?2 }",
            "{ '$limit': ?3 }"
    })
    Flux<GanadorDTO> findGanadoresByPremioAndAnyo(String premioId, Integer anyo, long skip, int limit);

    // El conteo también debe hacerse sobre los premios "unwinded" para ser exacto
    @Aggregation(pipeline = {
            "{ '$match': { 'premios': { '$elemMatch': { 'premioId': ?0, 'anio': ?1 } } } }",
            "{ '$unwind': '$premios' }",
            "{ '$match': { 'premios.premioId': ?0, 'premios.anio': ?1 } }",
            "{ '$count': 'total' }"
    })
    Mono<Long> countGanadoresByPremioAndAnyo(String premioId, Integer anyo);
}
