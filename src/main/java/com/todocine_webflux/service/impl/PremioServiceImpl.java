package com.todocine_webflux.service.impl;

import com.todocine_webflux.dao.CategoriaDAO;
import com.todocine_webflux.dao.GanadorRepo;
import com.todocine_webflux.dao.MovieDAO;
import com.todocine_webflux.dao.PremioDAO;
import com.todocine_webflux.dto.GanadorDTO;
import com.todocine_webflux.entities.Categoria;
import com.todocine_webflux.entities.Movie;
import com.todocine_webflux.entities.Premio;
import com.todocine_webflux.service.PremioService;
import com.todocine_webflux.utils.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PremioServiceImpl implements PremioService {

    @Autowired
    private GanadorRepo ganadorRepo;

    @Autowired
    private PremioDAO premioDAO;

    @Autowired
    private CategoriaDAO categoriaDAO;

    @Autowired
    private MovieDAO movieDAO;

    @Override
    public Mono<Paginator<GanadorDTO>> getPremioByCodigoAnyo(String premioId, Integer anyo, Integer page) {
        int size = 21;
        int offset = (page - 1) * size;

        Mono<List<GanadorDTO>> resultadosMono = ganadorRepo.findByPremioIdAndAnyo(premioId, anyo, offset, size)
                .flatMap(ganador -> {
                    Mono<Premio> premioMono = premioDAO.findById(ganador.getPremioId()).defaultIfEmpty(new Premio());
                    Mono<Categoria> categoriaMono = categoriaDAO.findById(ganador.getCategoriaId()).defaultIfEmpty(new Categoria());
                    Mono<Movie> movieMono = movieDAO.findById(ganador.getMovieId()).defaultIfEmpty(new Movie());

                    return Mono.zip(premioMono, categoriaMono, movieMono)
                            .map(tuple -> new GanadorDTO(ganador, tuple.getT1(), tuple.getT2(), tuple.getT3()));
                })
                .collectList();

        Mono<Long> count = ganadorRepo.countByPremioIdAndAnyo(premioId, anyo);

        return Mono.zip(resultadosMono, count)
                .map(tuple -> {
                    List<GanadorDTO> resultados = tuple.getT1();
                    Long total = tuple.getT2();

                    Paginator<GanadorDTO> paginator = new Paginator<>();
                    paginator.setPage(page);
                    paginator.setResults(resultados);
                    paginator.setTotalResults(total.intValue());
                    paginator.setTotalPages((int) (total / (size + 1)) + 1);

                    return paginator;
                });
    }

}
