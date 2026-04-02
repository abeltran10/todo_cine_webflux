package com.todocine_webflux.service;

import com.todocine_webflux.dto.response.GanadorDTO;
import com.todocine_webflux.dto.response.PremioDTO;
import com.todocine_webflux.utils.Paginator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PremioService {

    Flux<PremioDTO> getPremios();

    Mono<PremioDTO> getPremioById(String premioId);
}
