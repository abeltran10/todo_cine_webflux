package com.todocine_webflux.service.impl;

import com.todocine_webflux.dao.CategoriaPremioDAO;
import com.todocine_webflux.dto.response.CategoriaDTO;
import com.todocine_webflux.dto.response.PremioDTO;
import com.todocine_webflux.exceptions.NotFoudException;
import com.todocine_webflux.service.CategoriaPremioService;
import com.todocine_webflux.utils.mappers.CategoriaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.todocine_webflux.config.Constants.PREMIO_NOTFOUND;

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

    @Override
    public Flux<PremioDTO> getPremios() {
        return categoriaPremioDAO.findAll()
                .switchIfEmpty(Mono.empty())
                .map(categoriaPremio -> {
                    PremioDTO premioDTO = new PremioDTO(categoriaPremio.getPremioId());
                    premioDTO.setTitulo(categoriaPremio.getTitulo());
                    premioDTO.setAnyos(categoriaPremio.getAnyos());

                    return premioDTO;
                });
    }

    @Override
    public Mono<PremioDTO> getPremioById(Long premioId) {
        return categoriaPremioDAO.findByPremioId(premioId)
                .switchIfEmpty(Mono.error(new NotFoudException(PREMIO_NOTFOUND)))
                .map(categoriaPremio -> {
                    PremioDTO premioDTO = new PremioDTO(categoriaPremio.getPremioId());
                    premioDTO.setTitulo(categoriaPremio.getTitulo());
                    premioDTO.setAnyos(categoriaPremio.getAnyos());

                    return premioDTO;
                });
    }
}
