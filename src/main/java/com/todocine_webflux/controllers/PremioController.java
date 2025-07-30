package com.todocine_webflux.controllers;

import com.todocine_webflux.dto.GanadorDTO;
import com.todocine_webflux.service.PremioService;
import com.todocine_webflux.utils.Paginator;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/premios")
public class PremioController {

    @Autowired
    private PremioService premioService;


    @GetMapping("/{id}/anyos/{anyo}")
    public Mono<ResponseEntity<Paginator<GanadorDTO>>> getPremioByCodigoAnyo(@NotNull @PathVariable("id") String id,
                                                                             @NotNull @PathVariable("anyo") Integer anyo,
                                                                             @NotNull @RequestParam("pagina") Integer page) {
        return premioService.getPremioByCodigoAnyo(id, anyo, page)
                .map(ResponseEntity::ok);


    }
}
