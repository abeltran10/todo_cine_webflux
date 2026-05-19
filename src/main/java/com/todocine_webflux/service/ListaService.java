package com.todocine_webflux.service;

import com.todocine_webflux.dto.request.ListaReqDTO;
import com.todocine_webflux.dto.response.ListaDTO;
import com.todocine_webflux.utils.Paginator;
import reactor.core.publisher.Mono;

public interface ListaService {

    Mono<ListaDTO> getListaById(String id);

    Mono<ListaDTO> createLista(String userId, ListaReqDTO listaDTO);

    Mono<ListaDTO> updateLista(String id, String userId, ListaReqDTO listaDTO);

    Mono<Void> deleteLista(String id, String userId);

    Mono<Paginator<ListaDTO>> getListas(String userId, Integer page);

    Mono<ListaDTO> addMovieToList(String userId, String listaId, Long movieId);

    Mono<Void> deleteMovieFromList(String userId, String listaId, Long movieId);

    Mono<Paginator<ListaDTO>> getListasPublicas(Integer page);
}
