package com.todocine_webflux.controllers;

import com.todocine_webflux.dto.response.ListaDTO;
import com.todocine_webflux.service.ListaService;
import com.todocine_webflux.utils.Paginator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/listas")
public class ListaController {

    @Autowired
    private ListaService listaService;

    @GetMapping
    public Mono<ResponseEntity<Paginator<ListaDTO>>> getListasPublicas(
            @NotNull @RequestParam("page") Integer page) {

        return listaService.getListasPublicas(page)
                .map(paginator -> new ResponseEntity<>(paginator, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ListaDTO>> getListaById(@NotBlank @PathVariable("id") String listaId) {

        return listaService.getListaById(listaId)
                .map(listaDTO -> new ResponseEntity<>(listaDTO, HttpStatus.OK));
    }
}
