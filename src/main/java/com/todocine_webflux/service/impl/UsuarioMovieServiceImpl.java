package com.todocine_webflux.service.impl;

import com.todocine_webflux.config.Constants;
import com.todocine_webflux.dao.MovieDAO;
import com.todocine_webflux.dao.UsuarioMovieDAO;
import com.todocine_webflux.dao.UsuarioMovieRepo;
import com.todocine_webflux.dto.MovieDTO;
import com.todocine_webflux.dto.MovieDetailDTO;
import com.todocine_webflux.dto.UsuarioMovieDTO;
import com.todocine_webflux.entities.Movie;
import com.todocine_webflux.entities.UsuarioMovie;
import com.todocine_webflux.exceptions.NotFoudException;
import com.todocine_webflux.service.TMDBService;
import com.todocine_webflux.service.UsuarioMovieService;
import com.todocine_webflux.utils.Paginator;
import com.todocine_webflux.utils.mappers.MovieMapper;
import com.todocine_webflux.utils.mappers.UsuarioMovieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;

import static com.todocine_webflux.config.Constants.FAVORITOS_NOTFOUND;
import static com.todocine_webflux.config.Constants.MOVIE_NOTFOUND;

@Service
public class UsuarioMovieServiceImpl extends BaseServiceImpl implements UsuarioMovieService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioMovieServiceImpl.class);

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private UsuarioMovieRepo usuarioMovieRepo;

    @Autowired
    private MovieDAO movieDAO;

    @Autowired
    private UsuarioMovieDAO usuarioMovieDAO;


    @Override
    public Mono<Paginator<MovieDetailDTO>> getUsuarioMovies(String userId,
                                                            Map<String, String> filters,
                                                            String orderBy,
                                                            Integer page) {

        int limit  = 21;
        int offset = (page - 1) * limit;

        return checkCurrentUser(userId)
                .flatMapMany(ok -> usuarioMovieRepo
                        .findWithFilters(userId, filters, orderBy, offset, limit))
                .flatMap(this::toMovieDetail)
                .collectList()
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return Mono.error(new NotFoudException(FAVORITOS_NOTFOUND));
                    }
                    return usuarioMovieRepo.countWithFilters(userId, filters)
                            .map(total -> {
                                Paginator<MovieDetailDTO> p = new Paginator<>();
                                p.setResults(list);
                                p.setTotalResults(total.intValue());
                                p.setTotalPages((int) (total / (limit + 1)) + 1);
                                p.setPage(page);
                                return p;
                            });
                });
    }


    @Override
    public Mono<MovieDetailDTO> updateUsuarioMovie(String userId,
                                                   String movieId,
                                                   UsuarioMovieDTO dto) {

        return checkCurrentUser(userId)
                .flatMap(ok -> tmdbService.getMovieById(dto.getMovieId()))
                .switchIfEmpty(Mono.error(new NotFoudException(MOVIE_NOTFOUND)))
                .flatMap(movieMap -> upsertMovie(movieMap, movieId))
                .flatMap(movie -> upsertUsuarioMovie(userId, movie, dto))
                .flatMap(movie -> buildDetailDTO(movie, dto));
    }


    private Mono<MovieDetailDTO> toMovieDetail(UsuarioMovie um) {
        return movieDAO.findById(um.getMovieId())
                .switchIfEmpty(Mono.empty())
                .map(movie ->
                     new MovieDetailDTO(
                            MovieMapper.toDTO(movie),
                            true,
                            um.getVoto(),
                            "S".equals(um.getVista()))
                );


    }
    
    private Mono<Movie> upsertMovie(Map<String, Object> movieMap, String movieId) {
        MovieDTO movieDTO = MovieMapper.toDTO(movieMap);

        return movieDAO.findById(movieId)
                .switchIfEmpty(Mono.defer(() -> {
                    Movie newMovie = MovieMapper.toEntity(movieDTO);
                    return movieDAO.save(newMovie);
                }));
    }

    private Mono<Movie> upsertUsuarioMovie(String userId,
                                                                 Movie movie,
                                                                 UsuarioMovieDTO dto) {

        return usuarioMovieDAO.findByUsuarioIdAndMovieId(userId, movie.getId())
                .flatMap(existingUM -> {
                    Double oldVote = existingUM.getVoto();

                    updateVoteStats(movie, dto.getVoto(), oldVote);

                    existingUM.setFavoritos(dto.getFavoritos() ? "S" : "N");
                    existingUM.setVista(dto.getVista() ? "S" : "N");
                    existingUM.setVoto(dto.getVoto() != null ? dto.getVoto() : oldVote);

                    return usuarioMovieDAO.save(existingUM)
                            .then(movieDAO.save(movie))
                            .thenReturn(movie);
                })
                .switchIfEmpty(
                        Mono.defer(() -> {
                            UsuarioMovie newUM = UsuarioMovieMapper.toEntity(dto);

                            updateVoteStats(movie, dto.getVoto(), null);

                            return usuarioMovieDAO.save(newUM)
                                    .then(movieDAO.save(movie))
                                    .thenReturn(movie);
                        })
                );
    }

    private void updateVoteStats(Movie movie, Double newVote, Double oldVote) {
        if (newVote == null || newVote.equals(0.0)) return;

        if (oldVote != null && !oldVote.equals(0.0)) {
            // actualiza media manteniendo total
            double totalOld = movie.getVotosMediaTC() * movie.getTotalVotosTC() - oldVote;
            movie.setVotosMediaTC(round1Dec((totalOld + newVote) / movie.getTotalVotosTC()));
        } else {
            // primer voto de este usuario
            int total = movie.getTotalVotosTC() + 1;
            double totalSum = movie.getVotosMediaTC() * movie.getTotalVotosTC() + newVote;
            movie.setTotalVotosTC(total);
            movie.setVotosMediaTC(round1Dec(totalSum / total));
        }
    }

    private double round1Dec(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    private Mono<MovieDetailDTO> buildDetailDTO(Movie movie, UsuarioMovieDTO dto) {
        MovieDTO movieDTO = MovieMapper.toDTO(movie);
        return usuarioMovieDAO.findByUsuarioIdAndMovieId(dto.getUsuarioId(), movie.getId())
                .map(usuarioMovie -> new MovieDetailDTO(movieDTO, usuarioMovie.getFavoritos().equals("S"), usuarioMovie.getVoto(), usuarioMovie.getVista().equals("S"))
                );

    }
}
