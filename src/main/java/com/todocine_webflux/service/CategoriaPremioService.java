package com.todocine_webflux.service;

import com.todocine_webflux.dto.response.CategoriaDTO;
import com.todocine_webflux.dto.response.PremioDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoriaPremioService {

    Flux<CategoriaDTO> getCategorias(Long premioId);

    Flux<PremioDTO> getPremios();

    Mono<PremioDTO> getPremioById(Long premioId);
}
