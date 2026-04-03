package com.todocine_webflux.service;

import com.todocine_webflux.dto.request.GanadorReqDTO;
import com.todocine_webflux.dto.response.GanadorDTO;
import com.todocine_webflux.dto.response.MovieDTO;
import com.todocine_webflux.utils.Paginator;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface GanadorService {
    Mono<Paginator<GanadorDTO>> getGanadoresByPremioIdAnyo(Long premioId, Integer anyo, Integer page);

    Mono<GanadorDTO> insertGanador(GanadorReqDTO req);
}
