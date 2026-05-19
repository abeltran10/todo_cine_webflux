package com.todocine_webflux.service.impl;

import com.todocine_webflux.config.Constants;
import com.todocine_webflux.dao.ListaDAO;
import com.todocine_webflux.dao.MovieDAO;
import com.todocine_webflux.dao.UsuarioDAO;
import com.todocine_webflux.dto.request.ListaReqDTO;
import com.todocine_webflux.dto.response.ListaDTO;
import com.todocine_webflux.dto.response.MovieDTO;
import com.todocine_webflux.entities.Lista;
import com.todocine_webflux.entities.Movie;
import com.todocine_webflux.entities.MovieLista;
import com.todocine_webflux.entities.Usuario;
import com.todocine_webflux.exceptions.BadGatewayException;
import com.todocine_webflux.exceptions.BadRequestException;
import com.todocine_webflux.exceptions.ForbiddenException;
import com.todocine_webflux.exceptions.NotFoudException;
import com.todocine_webflux.service.ListaService;
import com.todocine_webflux.service.TMDBService;
import com.todocine_webflux.utils.Paginator;
import com.todocine_webflux.utils.mappers.ListaMapper;
import com.todocine_webflux.utils.mappers.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.todocine_webflux.config.Constants.*;

@Service
public class ListaServiceImpl extends BaseServiceImpl implements ListaService {

    @Autowired
    private ListaDAO listaDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private MovieDAO movieDAO;

    @Autowired
    private TMDBService tmdbService;


    @Override
    public Mono<Paginator<ListaDTO>> getListas(String userId, Integer page) {

        return usuarioDAO.findById(userId)
                .switchIfEmpty(Mono.error(new ForbiddenException(USER_FORBIDDEN)))
                .flatMap(usuario -> {
                    String username = usuario.getUsername();
                    Pageable pageable = PageRequest.of(page - 1, 10);

                    // Ejecutamos la búsqueda de la página y el conteo en paralelo usando el username
                    Mono<List<ListaDTO>> resultsMono = listaDAO.findByUsername(username, pageable)
                            .map(ListaMapper::toDTO)
                            .collectList();

                    Mono<Long> countMono = listaDAO.countByUsername(username);

                    // Combinamos ambos resultados asíncronos cuando terminen
                    return resultsMono.zipWith(countMono)
                            .map(tuple -> {
                                List<ListaDTO> results = tuple.getT1();
                                Long totalResults = tuple.getT2();
                                int totalPages = (int) Math.ceil((double) totalResults / 10);

                                Paginator<ListaDTO> paginator = new Paginator<>();
                                paginator.setResults(results);
                                paginator.setPage(page);
                                paginator.setTotalPages(totalPages);
                                paginator.setTotalResults(totalResults.intValue());
                                return paginator;
                            });
                });
    }

    @Override
    public Mono<ListaDTO> getListaById(String id) {
        // 1. Buscamos primero la lista para saber si es pública o privada
        return listaDAO.findById(id)
                .switchIfEmpty(Mono.error(new NotFoudException(LISTA_NOT_FOUND)))
                .flatMap(lista -> {

                    // Caso A: La lista es pública. Cualquier visitante puede verla sin estar logueado.
                    if ("S".equals(lista.getPublica())) {
                        return Mono.just(ListaMapper.toDTO(lista));
                    }

                    // Caso B: La lista es privada. Necesitamos validar la sesión de forma reactiva.
                    return getCurrentUserId()
                            // Usamos switchIfEmpty por si getCurrentUserId() viene vacío (no logueado)
                            .switchIfEmpty(Mono.error(new ForbiddenException(USER_FORBIDDEN)))
                            .flatMap(userId -> usuarioDAO.findById(userId))
                            .switchIfEmpty(Mono.error(new ForbiddenException(USER_FORBIDDEN)))
                            .flatMap(usuario -> {
                                // Comparamos el dueño de la lista con el usuario logueado
                                if (lista.getUsername().equals(usuario.getUsername())) {
                                    return Mono.just(ListaMapper.toDTO(lista));
                                } else {
                                    return Mono.error(new ForbiddenException(USER_FORBIDDEN));
                                }
                            });
                });
    }

    @Override
    public Mono<ListaDTO> createLista(String userId, ListaReqDTO listaDTO) {
        return usuarioDAO.findById(userId)
                .switchIfEmpty(Mono.error(new ForbiddenException(USER_FORBIDDEN)))
                .flatMap(usuario -> {
                    Lista lista = new Lista();
                    lista.setNombre(listaDTO.getNombre());
                    lista.setDescripcion(listaDTO.getDescripcion());
                    lista.setUsername(usuario.getUsername());
                    lista.setPublica("N");

                    return listaDAO.save(lista);
                })
                .map(ListaMapper::toDTO);
    }

    @Override
    public Mono<ListaDTO> updateLista(String id, String userId, ListaReqDTO listaDTO) {
        if (!id.equals(listaDTO.getId())) {
            return Mono.error(new BadRequestException(ID_NOT_MATCH));
        }

        // 1. Buscamos al usuario por su ID para conocer su username real
        return usuarioDAO.findById(userId)
                .switchIfEmpty(Mono.error(new ForbiddenException(USER_FORBIDDEN)))
                .flatMap(usuario -> {
                    String usernameDelSolicitante = usuario.getUsername();

                    return listaDAO.findById(id)
                            .switchIfEmpty(Mono.error(new NotFoudException(LISTA_NOT_FOUND)))
                            .flatMap(listaExistente -> {

                                if (!listaExistente.getUsername().equals(usernameDelSolicitante)) {
                                    return Mono.error(new ForbiddenException(USER_FORBIDDEN));
                                }

                                listaExistente.setNombre(listaDTO.getNombre());
                                listaExistente.setDescripcion(listaDTO.getDescripcion());
                                listaExistente.setPublica(listaDTO.getPublica() != null && listaDTO.getPublica() ? "S" : "N");

                                return listaDAO.save(listaExistente);
                            });
                })
                .map(ListaMapper::toDTO);
    }

    @Override
    public Mono<Void> deleteLista(String id, String userId) {
        return usuarioDAO.findById(userId)
                        .switchIfEmpty(Mono.error(new ForbiddenException(USER_FORBIDDEN)))
                        .flatMap(usuario -> {
                            String usernameDelSolicitante = usuario.getUsername();

                            return listaDAO.findById(id)
                                    .switchIfEmpty(Mono.error(new NotFoudException(LISTA_NOT_FOUND)))
                                    .flatMap(lista -> {
                                        if (!lista.getUsername().equals(usernameDelSolicitante)) {
                                            return Mono.error(new ForbiddenException(USER_FORBIDDEN));
                                        }
                                        return listaDAO.delete(lista);
                                    });
                        });

    }

    @Override
    public Mono<ListaDTO> addMovieToList(String userId, String listaId, Long movieId) {
        return usuarioDAO.findById(userId)
                .switchIfEmpty(Mono.error(new ForbiddenException(USER_FORBIDDEN)))
                .flatMap(usuario -> {
                    String usernameDelSolicitante = usuario.getUsername();

                    return listaDAO.findById(listaId)
                            .switchIfEmpty(Mono.error(new NotFoudException(LISTA_NOT_FOUND)))
                            .flatMap(lista -> {
                                if (!lista.getUsername().equals(usernameDelSolicitante)) {
                                    return Mono.error(new ForbiddenException(USER_FORBIDDEN));
                                }

                                // Verificamos si la película ya existe en nuestro subdocumento embebido para evitar duplicados
                                boolean yaExiste = lista.getMovies().stream()
                                        .anyMatch(preview -> preview.getId().equals(movieId));

                                if (yaExiste) {
                                    return Mono.just(lista);
                                }

                                return movieDAO.findMovieById(movieId)
                                        .map(movie -> {
                                            MovieLista movieLista = new MovieLista(movie.getId());
                                            movieLista.setTitle(movie.getTitle());
                                            movieLista.setPosterPath(movie.getPosterPath());
                                            movieLista.setReleaseDate(movie.getReleaseDate());

                                            return movieLista;
                                        })
                                        .switchIfEmpty(Mono.defer(() -> fetchMovieFromTMDB(movieId)))
                                        .flatMap(moviePreview -> {
                                            lista.getMovies().add(moviePreview);
                                            return Mono.just(lista);
                                        });
                            })
                            .flatMap(listaDAO::save)
                            .map(ListaMapper::toDTO);
                });

    }

    // Método auxiliar reactivo para consumir WebClient de forma no bloqueante
    private Mono<MovieLista> fetchMovieFromTMDB(Long movieId) {
        return tmdbService.getMovieById(String.valueOf(movieId))
                .onErrorMap(e -> new BadGatewayException(TMDB_ERROR)) // Transforma fallos de red en BadGateway de inmediato
                .flatMap(movieMap -> {
                    if (movieMap.get("id") == null) {
                        return Mono.error(new NotFoudException(MOVIE_NOTFOUND));
                    }
                    // Mapeamos los datos, salvamos la entidad completa en la colección maestra 'movies' y retornamos el Preview
                    MovieDTO movieDTO = MovieMapper.toDTO(movieMap);
                    return movieDAO.save(MovieMapper.toEntity(movieDTO))
                            .map(savedMovie -> {
                                        MovieLista movieLista = new MovieLista(savedMovie.getId());
                                        movieLista.setTitle(savedMovie.getTitle());
                                        movieLista.setPosterPath(savedMovie.getPosterPath());
                                        movieLista.setReleaseDate(savedMovie.getReleaseDate());

                                        return movieLista;
                            });
                });
    }

    @Override
    public Mono<Void> deleteMovieFromList(String userId, String listaId, Long movieId) {
        return usuarioDAO.findById(userId)
                .switchIfEmpty(Mono.error(new ForbiddenException(USER_FORBIDDEN)))
                .flatMap(usuario -> {
                    String usernameDelSolicitante = usuario.getUsername();

                    return listaDAO.findById(listaId)
                            .switchIfEmpty(Mono.error(new NotFoudException(LISTA_NOT_FOUND)))
                            .flatMap(lista -> {
                                if (!lista.getUsername().equals(usernameDelSolicitante)) {
                                    return Mono.error(new ForbiddenException(USER_FORBIDDEN));
                                }

                                boolean removed = lista.getMovies().removeIf(movie -> movie.getId().equals(movieId));
                                if (!removed) {
                                    return Mono.error(new NotFoudException(MOVIE_NOTFOUND));
                                }

                                return listaDAO.save(lista).then();
                            });
                });

    }

    @Override
    public Mono<Paginator<ListaDTO>> getListasPublicas(Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10);

        return listaDAO.findByPublica("S", pageable)
                .map(ListaMapper::toDTO)
                .collectList()
                .zipWith(listaDAO.countByPublica("S"))
                .map(tuple -> {
                    List<ListaDTO> results = tuple.getT1();
                    Long totalResults = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalResults / 10);

                    Paginator<ListaDTO> paginator = new Paginator<>();
                    paginator.setResults(results);
                    paginator.setPage(page);
                    paginator.setTotalPages(totalPages);
                    paginator.setTotalResults(totalResults.intValue());
                    return paginator;
                });
    }
}
