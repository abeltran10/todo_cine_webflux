package com.todocine_webflux.controllers;

import com.todocine_webflux.dto.response.CategoriaDTO;
import com.todocine_webflux.dto.response.PremioDTO;
import com.todocine_webflux.service.CategoriaPremioService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/premios")
public class PremioController {

    @Autowired
    private CategoriaPremioService categoriaPremioService;

    @GetMapping
    public Flux<PremioDTO> getPremios() {
        // Flux emite N elementos uno a uno.
        return categoriaPremioService.getPremios();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PremioDTO>> getPremioById(@NotNull @PathVariable("id") Long id) {
        return categoriaPremioService.getPremioById(id)
                .map(ResponseEntity::ok);

    }

    @GetMapping("/{id}/categorias")
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<CategoriaDTO> getCategorias(@NotNull @PathVariable("id") Long id) {
        return categoriaPremioService.getCategorias(id);
    }
}
