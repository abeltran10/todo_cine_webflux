package com.todocine_webflux.service.impl;

import com.todocine_webflux.dao.CategoriaPremioDAO;
import com.todocine_webflux.dao.MovieDAO;
import com.todocine_webflux.dto.response.PremioDTO;
import com.todocine_webflux.exceptions.NotFoudException;
import com.todocine_webflux.service.PremioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.todocine_webflux.config.Constants.PREMIO_NOTFOUND;

@Service
public class PremioServiceImpl implements PremioService {

    @Autowired
    private CategoriaPremioDAO categoriaDAO;

    @Autowired
    private MovieDAO movieDAO;

    @Override
    public Flux<PremioDTO> getPremios() {
        return movieDAO.findDistinctPremiosConAnyos();
    }

    @Override
    public Mono<PremioDTO> getPremioById(Long premioId) {
        return movieDAO.findPremioById(premioId)
                // Si la agregación no devuelve nada (Mono vacío), lanzamos excepción
                .switchIfEmpty(Mono.error(new NotFoudException(PREMIO_NOTFOUND)));
    }



}
