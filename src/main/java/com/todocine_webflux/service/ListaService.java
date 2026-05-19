package com.todocine_webflux.service;

import com.todocine_webflux.dto.request.ListaReqDTO;
import com.todocine_webflux.dto.response.ListaDTO;
import com.todocine_webflux.utils.Paginator;
import reactor.core.publisher.Mono;

public interface ListaService {

    Mono<ListaDTO> getListaById(String id);

    Mono<ListaDTO> createLista(ListaReqDTO listaDTO);

    Mono<ListaDTO> updateLista(String id, ListaReqDTO listaDTO);

    Mono<Void> deleteLista(String id);

    Mono<Paginator<ListaDTO>> getListas(String userId, Integer page);

    Mono<ListaDTO> addMovieToList(String listaId, Long movieId);

    Mono<Void> deleteMovieFromList(String listaId, Long movieId);

    Mono<Paginator<ListaDTO>> getListasPublicas(Integer page);
}
