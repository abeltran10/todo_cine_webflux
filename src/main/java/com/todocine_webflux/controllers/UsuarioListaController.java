package com.todocine_webflux.controllers;

import com.todocine_webflux.dto.request.ListaReqDTO;
import com.todocine_webflux.dto.response.ListaDTO;
import com.todocine_webflux.service.ListaService;
import com.todocine_webflux.utils.Paginator;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios/{userId}/listas")
public class UsuarioListaController {

    @Autowired
    private ListaService listaService;

    @GetMapping
    public Mono<ResponseEntity<Paginator<ListaDTO>>> getListas(
           @NotBlank @PathVariable("userId") String userId,
           @NotNull @RequestParam("page") Integer page) {

        return listaService.getListas(userId, page)
                .map(paginator -> new ResponseEntity<>(paginator, HttpStatus.OK));
    }

    @PostMapping
    public Mono<ResponseEntity<ListaDTO>> createLista(
            @NotBlank @PathVariable("userId") String userId,
            @Valid @RequestBody ListaReqDTO listaDTO) {

        return listaService.createLista(userId, listaDTO)
                .map(createdLista -> new ResponseEntity<>(createdLista, HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ListaDTO>> updateLista(
            @NotBlank @PathVariable("userId") String userId,
            @NotBlank @PathVariable("id") String id,
            @Valid @RequestBody ListaReqDTO listaDTO) {

        return listaService.updateLista(id, userId, listaDTO)
                .map(updatedLista -> new ResponseEntity<>(updatedLista, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLista(
            @NotBlank @PathVariable("userId") String userId,
            @NotBlank @PathVariable("id") String id) {

        return listaService.deleteLista(id, userId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }

    @PostMapping("/{listaId}/movies/{movieId}")
    public Mono<ResponseEntity<ListaDTO>> addMovieToList(
            @NotBlank @PathVariable("userId") String userId,
            @NotBlank @PathVariable("listaId") String listaId,
            @NotNull @PathVariable("movieId") Long movieId) {

        return listaService.addMovieToList(userId, listaId, movieId)
                .map(updatedLista -> new ResponseEntity<>(updatedLista, HttpStatus.OK));
    }

    @DeleteMapping("/{listaId}/movies/{movieId}")
    public Mono<ResponseEntity<Void>> deleteMovieFromList(
            @NotBlank @PathVariable("userId") String userId,
            @NotBlank @PathVariable("listaId") String listaId,
            @NotNull @PathVariable("movieId") Long movieId) {

        return listaService.deleteMovieFromList(userId, listaId, movieId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }
}
