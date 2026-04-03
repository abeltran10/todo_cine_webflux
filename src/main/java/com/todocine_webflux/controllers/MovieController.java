package com.todocine_webflux.controllers;

import com.todocine_webflux.config.Constants;
import com.todocine_webflux.dto.response.MovieDTO;
import com.todocine_webflux.dto.response.MovieDetailDTO;
import com.todocine_webflux.service.MovieService;
import com.todocine_webflux.utils.Paginator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public Mono<ResponseEntity<Paginator<MovieDTO>>> getMovies(@RequestParam("name") String name,
                                                               @RequestParam("status") String status,
                                                               @RequestParam("region") String region,
                                                               @RequestParam("page") Integer pagina) {

        Map<String, String> filters = new HashMap<>();
        filters.put(Constants.MOVIE_NAME, name);
        filters.put(Constants.MOVIE_STATUS, status);
        filters.put(Constants.MOVIE_REGION, region);

        return movieService.getMovies(filters, pagina)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovieDetailDTO>> getMovieDetailById(@NotNull @PathVariable("id") Long id) {
        return movieService.getMovieDetailById(id)
                .map(ResponseEntity::ok);
    }
}
