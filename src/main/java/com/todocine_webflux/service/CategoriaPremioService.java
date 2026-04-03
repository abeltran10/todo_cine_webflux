package com.todocine_webflux.service;

import com.todocine_webflux.dto.response.CategoriaDTO;
import reactor.core.publisher.Flux;

public interface CategoriaPremioService {

    Flux<CategoriaDTO> getCategorias(Long premioId);
}
