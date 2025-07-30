package com.todocine_webflux.service;

import com.todocine_webflux.dto.GanadorDTO;
import com.todocine_webflux.utils.Paginator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PremioService {

    Mono<Paginator<GanadorDTO>> getPremioByCodigoAnyo(String premioId, Integer anyo, Integer page);
}
