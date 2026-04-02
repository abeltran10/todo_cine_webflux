package com.todocine_webflux.controllers;


import com.todocine_webflux.dto.response.MovieDetailDTO;
import com.todocine_webflux.dto.request.UsuarioMovieDTO;
import com.todocine_webflux.service.UsuarioMovieService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios/{usuarioId}/movies")
public class UsuarioMovieController {

    @Autowired
    private UsuarioMovieService usuarioMovieService;

    @PutMapping("/{movieId}")
    public Mono<ResponseEntity<MovieDetailDTO>> updateUsuarioMovie(@NotBlank @PathVariable("usuarioId") Long userId,
                                                                   @NotBlank @PathVariable("movieId") String movieId,
                                                                   @Valid @RequestBody UsuarioMovieDTO usuarioMovieDTO) {
        return usuarioMovieService.updateUsuarioMovie(userId, movieId, usuarioMovieDTO)
                .map(ResponseEntity::ok);

    }
}
