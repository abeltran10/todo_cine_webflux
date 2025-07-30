package com.todocine_webflux.service.impl;

import com.todocine_webflux.dao.MovieDAO;
import com.todocine_webflux.dao.UsuarioMovieDAO;
import com.todocine_webflux.dto.MovieDTO;
import com.todocine_webflux.dto.MovieDetailDTO;
import com.todocine_webflux.entities.Movie;
import com.todocine_webflux.entities.Usuario;
import com.todocine_webflux.entities.UsuarioMovie;
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
    public Mono<MovieDetailDTO> getMovieDetailById(String id) {
        return tmdbService.getMovieById(id)
                .flatMap(movieMap -> {
                    if (movieMap.get("id") == null) {
                        return Mono.error(new NotFoudException(MOVIE_NOTFOUND));
                    }

                    MovieDTO movieDTO = MovieMapper.toDTO(movieMap);

                    return movieDAO.findById(id)
                            .flatMap(dbMovie -> {
                                // dbMovie existe
                                MovieDTO dbMovieDTO = MovieMapper.toDTO(dbMovie);
                                movieDTO.setVotosMediaTC(dbMovieDTO.getVotosMediaTC());
                                movieDTO.setTotalVotosTC(dbMovieDTO.getTotalVotosTC());
                                return Mono.just(movieDTO);
                            })
                            .switchIfEmpty(Mono.just(movieDTO)) // si no existe en BD, seguimos con el movieDTO original
                            .flatMap(updatedMovieDTO ->
                                    getCurrentUserId()
                                            .flatMap(userId -> usuarioMovieDAO.findByUsuarioIdAndMovieId(userId, id))
                                            .flatMap(um -> {
                                                boolean favorito = "S".equals(um.getFavoritos());
                                                boolean vista = "S".equals(um.getVista());
                                                Double voto = um.getVoto();
                                                return Mono.just(new MovieDetailDTO(updatedMovieDTO, favorito, voto, vista));
                                            })
                                            .switchIfEmpty(Mono.just(new MovieDetailDTO(updatedMovieDTO, false, null, false)))
                            );
                })
                .onErrorMap(IOException.class, ex -> new BadGatewayException(TMDB_ERROR));
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
