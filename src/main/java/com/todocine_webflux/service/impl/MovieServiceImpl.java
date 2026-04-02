package com.todocine_webflux.service.impl;

import com.todocine_webflux.dao.MovieDAO;
import com.todocine_webflux.dao.UsuarioMovieDAO;
import com.todocine_webflux.dto.response.MovieDTO;
import com.todocine_webflux.dto.response.MovieDetailDTO;
import com.todocine_webflux.exceptions.BadGatewayException;
import com.todocine_webflux.exceptions.BadRequestException;
import com.todocine_webflux.exceptions.NotFoudException;
import com.todocine_webflux.service.MovieService;
import com.todocine_webflux.service.TMDBService;
import com.todocine_webflux.utils.Paginator;
import com.todocine_webflux.utils.mappers.MovieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.todocine_webflux.config.Constants.*;

@Service
public class MovieServiceImpl extends BaseServiceImpl implements MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private MovieDAO movieDAO;

    @Autowired
    private UsuarioMovieDAO usuarioMovieDAO;

    @Override
    public Mono<MovieDetailDTO> getMovieDetailById(Long id) {
        // 1. Ejecutamos en paralelo: Pedir la peli a TMDB y obtener el Usuario del contexto
        return Mono.zip(tmdbService.getMovieById(String.valueOf(id)), getCurrentUserId())
                .switchIfEmpty(Mono.error(new NotFoudException(MOVIE_NOTFOUND)))
                .flatMap(tuple -> {
                    Map<String, Object> movieMap = tuple.getT1(); // Resultado de TMDB
                    Long userId = tuple.getT2();               // Resultado de getCurrentUserId()

                    MovieDTO movieDTO = MovieMapper.toDTO(movieMap);

                    // 2. Buscamos nuestros datos locales (Stats y Premios)
                    return movieDAO.findMovieById(id)
                            .map(movie -> {
                                movieDTO.setVotosMediaTC(movie.getVotosMediaTC());
                                movieDTO.setTotalVotosTC(movie.getTotalVotosTC());
                                // Aquí ya tendrías acceso a movie.getPremios()
                                return movieDTO;
                            })
                            .defaultIfEmpty(movieDTO) // Si no existe en nuestra DB local

                            // 3. Buscamos la interacción de ese usuario concreto con esa película
                            .flatMap(dto -> usuarioMovieDAO.findByUsuarioIdAndMovieId(userId, id)
                                    .map(um -> new MovieDetailDTO(
                                            dto,
                                            "S".equals(um.getFavoritos()),
                                            um.getVoto(),
                                            "S".equals(um.getVista())
                                    ))
                                    // Si no hay interacción previa, valores por defecto
                                    .switchIfEmpty(Mono.just(new MovieDetailDTO(dto, false, null, false)))
                            );
                })
                .onErrorMap(IOException.class, e -> new BadGatewayException(TMDB_ERROR));
    }

    @Override
    public Mono<Paginator<MovieDTO>> getMovies(Map<String, String> filters, Integer pagina) {
        Mono<Map<String, Object>> movieMap;

        if (!filters.get(MOVIE_NAME).isBlank()) {
            movieMap = tmdbService.getMoviesByName(filters.get(MOVIE_NAME), pagina);
        } else if (!filters.get(MOVIE_STATUS).isBlank() && !filters.get(MOVIE_REGION).isBlank()) {
            movieMap = tmdbService.getMoviesPlayingNow(filters.get(MOVIE_REGION), pagina);
        } else {
            return Mono.error(new BadRequestException(MOVIE_SEARCH_BADREQUEST));
        }

        return movieMap.flatMap(map -> {
            if (!map.containsKey("results")) {
                return Mono.error(new NotFoudException(MOVIE_NOTFOUND));
            }

            Paginator<MovieDTO> paginator = new Paginator<>(map);
            List<Map<String, Object>> rawResults = (List<Map<String, Object>>) map.get("results");

            List<MovieDTO> results = rawResults.stream()
                    .map(MovieMapper::toDTO)
                    .toList();

            paginator.setResults(results);
            logger.info(paginator.toString());

            return Mono.just(paginator);
        }).onErrorMap(IOException.class, e -> new BadGatewayException(TMDB_ERROR));
    }
}
