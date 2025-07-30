package com.todocine_webflux.controllers;

import com.todocine_webflux.config.Constants;
import com.todocine_webflux.dto.MovieDetailDTO;
import com.todocine_webflux.dto.UsuarioDTO;
import com.todocine_webflux.service.UsuarioMovieService;
import com.todocine_webflux.service.UsuarioService;
import com.todocine_webflux.utils.Paginator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMovieService usuarioMovieService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UsuarioDTO>> getUsuario(@NotBlank @PathVariable("id") String id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok);

    }

    @GetMapping
    public Mono<ResponseEntity<UsuarioDTO>> getUsuarios(@NotBlank @RequestParam("username") String username) {
        logger.info("getUsuarioByName controller");
        return usuarioService.getUsuarioByName(username)
                .map(ResponseEntity::ok);

    }

    @PostMapping
    public Mono<ResponseEntity<UsuarioDTO>> insertUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.insertUsuario(usuarioDTO)
                .map(usuario -> ResponseEntity.created(URI.create("")).body(usuario));

    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UsuarioDTO>> updateUsuario(@NotBlank @PathVariable("id") String id,
                                                          @Valid @RequestBody UsuarioDTO usuarioDTO) {
        logger.info("updateUsuario");
        return usuarioService.updateUsuario(id, usuarioDTO)
                .map(ResponseEntity::ok);

    }

    @GetMapping("/{userId}/movies")
    public Mono<ResponseEntity<Paginator<MovieDetailDTO>>> getUsuarioMovies(
            @NotBlank @PathVariable("userId") String userId,
            @RequestParam("vista") String vista,
            @RequestParam("votada") String votada,
            @RequestParam("orderBy") String orderBy,
            @RequestParam("page") Integer pagina) {

        Map<String, String> filters = new HashMap<>();
        filters.put(Constants.VISTA_FILTER, vista);
        filters.put(Constants.VOTADA_FILTER, votada);

        return usuarioMovieService.getUsuarioMovies(userId, filters, orderBy, pagina)
                .map(paginator -> ResponseEntity.ok(paginator));

    }
}
