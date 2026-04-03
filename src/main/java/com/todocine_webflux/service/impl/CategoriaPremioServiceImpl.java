package com.todocine_webflux.service.impl;

import com.todocine_webflux.dao.CategoriaPremioDAO;
import com.todocine_webflux.dto.response.CategoriaDTO;
import com.todocine_webflux.service.CategoriaPremioService;
import com.todocine_webflux.utils.mappers.CategoriaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoriaPremioServiceImpl implements CategoriaPremioService {

    @Autowired
    private CategoriaPremioDAO categoriaPremioDAO;

    @Override
    public Flux<CategoriaDTO> getCategorias(Long premioId) {
        return categoriaPremioDAO.findByPremioId(premioId)
                .switchIfEmpty(Mono.empty())
                .flatMapMany(doc -> Flux.fromIterable(doc.getCategorias()))
                .map(CategoriaMapper::toDTO);
    }
}
