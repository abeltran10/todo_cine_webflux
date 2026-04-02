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
    public Mono<Paginator<GanadorDTO>> getGanadoresByPremioIdAnyo(String premioId, Integer anyo, Integer page) {
        int size = 21;
        long skip = (long) page * 21;
        Paginator<GanadorDTO> paginator = new Paginator<>();

        // 1. Ejecutamos el conteo y la búsqueda en paralelo con Mono.zip
        return Mono.zip(
                        movieDAO.findGanadoresByPremioAndAnyo(premioId, anyo, skip, size)
                                .collectList(),          // Agrupamos en una lista para el paginador
                        movieDAO.countGanadoresByPremioAndAnyo(premioId, anyo)
                )
                .map(tuple -> {
                    List<GanadorDTO> movies = tuple.getT1();
                    Long totalElements = tuple.getT2();

                    paginator.setResults(movies);
                    paginator.setPage(page);
                    paginator.setTotalPages(totalElements.intValue());
                    paginator.setTotalResults(size);
                    // 2. Construimos nuestro objeto Paginator personalizado
                    return paginator;

                })
                .switchIfEmpty(Mono.just(paginator));
    }

    @Override
// Nota: Las transacciones en MongoDB requieren un setup específico (Replica Set)
    public Mono<GanadorDTO> insertGanador(GanadorReqDTO req) {
        String movieId = String.valueOf(req.getMovieId());
        String premioId = String.valueOf(req.getPremioId());

        // 1. Verificamos si la película existe en nuestra DB local
        return movieDAO.findById(movieId)
                // 2. Si no existe localmente, la buscamos en TMDB y la guardamos
                .switchIfEmpty(
                        tmdbService.getMovieById(String.valueOf(req.getMovieId()))
                                .switchIfEmpty(Mono.error(new NotFoudException(MOVIE_NOTFOUND)))
                                .map(MovieMapper::toDTO)
                                .map(MovieMapper::toEntity)
                                .flatMap(movieDAO::save)
                )
                .flatMap(movie -> {
                    // 3. Verificamos si ya tiene ese premio (Evitar duplicados / ConflictException)
                    boolean yaEsGanador = movie.getPremios() != null && movie.getPremios().stream()
                            .anyMatch(p -> p.getPremioId().equals(premioId)
                                    && p.getAnyo().equals(req.getAnyo())
                                    && p.getCategoriaId().equals(req.getCategoriaId()));

                    if (yaEsGanador) {
                        return Mono.error(new ConflictException(GANADOR_EXISTS));
                    }

                    // 4. Validamos que el Premio/Categoría existan en la colección maestra
                    return categoriaPremioDAO.findByPremioId(premioId) // Ajustado a tu DAO anterior
                            .switchIfEmpty(Mono.error(new NotFoudException(PREMIO_NOTFOUND)))
                            .flatMap(catPremio -> {
                                // 5. Creamos el nuevo objeto de premio ganado
                                Premio nuevoPremio = new Premio();
                                nuevoPremio.setPremioId(premioId);
                                nuevoPremio.setTitulo(catPremio.getTitulo()); // El título (ej: Oscar)
                                nuevoPremio.setAnyo(req.getAnyo());
                                nuevoPremio.setCategoriaId(String.valueOf(req.getCategoriaId()));
                                // Buscamos el nombre de la categoría dentro de la lista de catPremio
                                String nombreCat = catPremio.getCategorias().stream()
                                        .filter(c -> c.getId().equals(String.valueOf(req.getCategoriaId())))
                                        .map(Categoria::getNombre)
                                        .findFirst().get();
                                nuevoPremio.setCategoria(nombreCat);

                                // 6. Añadimos el premio a la película y guardamos
                                if (movie.getPremios() == null) movie.setPremios(new ArrayList<>());
                                movie.getPremios().add(nuevoPremio);

                                return movieDAO.save(movie)
                                        .map(m -> GanadorMapper.toDTO(m, nuevoPremio));
                            });
                })
                .onErrorMap(IOException.class, e -> new BadGatewayException(TMDB_ERROR));
    }


}
