package com.todocine_webflux.controllers;

import com.todocine_webflux.dto.request.GanadorReqDTO;
import com.todocine_webflux.dto.response.GanadorDTO;
import com.todocine_webflux.service.GanadorService;
import com.todocine_webflux.utils.Paginator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ganadores")
public class GanadorController {

    @Autowired
    private GanadorService ganadorService;

    @GetMapping
    public Mono<ResponseEntity<Paginator<GanadorDTO>>> getGanadoresByPremioIdAnyo(
            @NotNull @RequestParam("premioId") String id,
            @NotNull @RequestParam("anyo") Integer anyo,
            @NotNull @RequestParam("pagina") Integer page) {

        return ganadorService.getGanadoresByPremioIdAnyo(id, anyo, page)
                .map(paginador -> ResponseEntity.ok(paginador))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GanadorDTO> insertGanador(@Valid @RequestBody GanadorReqDTO ganadorReqDTO) {
        return ganadorService.insertGanador(ganadorReqDTO);
    }
}
