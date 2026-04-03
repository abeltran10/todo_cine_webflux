package com.todocine_webflux.service.impl;

import com.todocine_webflux.dao.CategoriaPremioDAO;
import com.todocine_webflux.dao.MovieDAO;
import com.todocine_webflux.dto.request.GanadorReqDTO;
import com.todocine_webflux.dto.response.GanadorDTO;
import com.todocine_webflux.dto.response.MovieDTO;
import com.todocine_webflux.entities.Categoria;
import com.todocine_webflux.entities.Premio;
import com.todocine_webflux.exceptions.BadGatewayException;
import com.todocine_webflux.exceptions.ConflictException;
import com.todocine_webflux.exceptions.NotFoudException;
import com.todocine_webflux.service.GanadorService;
import com.todocine_webflux.service.TMDBService;
import com.todocine_webflux.utils.Paginator;
import com.todocine_webflux.utils.mappers.GanadorMapper;
import com.todocine_webflux.utils.mappers.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.todocine_webflux.config.Constants.*;

@Service
public class GanadorServiceImpl implements GanadorService {

    @Autowired
    private MovieDAO movieDAO;

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private CategoriaPremioDAO categoriaPremioDAO;

    @Override
    public Mono<Paginator<GanadorDTO>> getGanadoresByPremioIdAnyo(Long premioId, Integer anyo, Integer page) {
        int size = 21;
        long skip = (long) (page - 1) * 21;
        Paginator<GanadorDTO> paginator = new Paginator<>();

        return Mono.zip(
                        movieDAO.findGanadoresByPremioAndAnyo(premioId, anyo, skip, size)
                                .collectList(),
                        movieDAO.countGanadoresByPremioAndAnyo(premioId, anyo)
                )
                .map(tuple -> {
                    List<GanadorDTO> movies = tuple.getT1();
                    Long totalElements = tuple.getT2();

                    paginator.setResults(movies);
                    paginator.setPage(page);
                    paginator.setTotalPages((int) Math.ceil((double) totalElements / size));
                    paginator.setTotalResults(totalElements.intValue());
                    // 2. Construimos nuestro objeto Paginator personalizado
                    return paginator;

                })
                .switchIfEmpty(Mono.just(paginator));
    }

    @Override
    public Mono<GanadorDTO> insertGanador(GanadorReqDTO req) {
        Long movieId = req.getMovieId();
        Long premioId = req.getPremioId();

        return movieDAO.findMovieById(movieId)
                .switchIfEmpty(
                        tmdbService.getMovieById(String.valueOf(req.getMovieId()))
                                .switchIfEmpty(Mono.error(new NotFoudException(MOVIE_NOTFOUND)))
                                .map(MovieMapper::toDTO)
                                .map(MovieMapper::toEntity)
                                .flatMap(movieDAO::save)
                )
                .flatMap(movie -> {
                        boolean yaEsGanador = movie.getPremios() != null && movie.getPremios().stream()
                            .anyMatch(p -> p.getPremioId().equals(premioId)
                                    && p.getAnyo().equals(req.getAnyo())
                                    && p.getCategoriaId().equals(req.getCategoriaId()));

                    if (yaEsGanador) {
                        return Mono.error(new ConflictException(GANADOR_EXISTS));
                    }

                    return categoriaPremioDAO.findByPremioId(premioId)
                            .switchIfEmpty(Mono.error(new NotFoudException(PREMIO_NOTFOUND)))
                            .flatMap(catPremio -> {
                                Premio nuevoPremio = new Premio();
                                nuevoPremio.setPremioId(premioId);
                                nuevoPremio.setTitulo(catPremio.getTitulo());
                                nuevoPremio.setAnyo(req.getAnyo());
                                nuevoPremio.setCategoriaId(req.getCategoriaId());

                                String nombreCat = catPremio.getCategorias().stream()
                                        .filter(c -> c.getId().equals(req.getCategoriaId()))
                                        .map(Categoria::getNombre)
                                        .findFirst().orElse(null);

                                if (nombreCat == null)
                                    return Mono.error(new NotFoudException(PREMIO_NOTFOUND));

                                nuevoPremio.setCategoria(nombreCat);

                                if (movie.getPremios() == null) movie.setPremios(new ArrayList<>());
                                movie.getPremios().add(nuevoPremio);

                                return movieDAO.save(movie)
                                        .map(m -> GanadorMapper.toDTO(m, nuevoPremio));
                            });
                })
                .onErrorMap(IOException.class, e -> new BadGatewayException(TMDB_ERROR));
    }


}
