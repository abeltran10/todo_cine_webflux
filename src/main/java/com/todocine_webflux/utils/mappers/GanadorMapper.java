package com.todocine_webflux.utils.mappers;

import com.todocine_webflux.dto.response.GanadorDTO;
import com.todocine_webflux.entities.Movie;
import com.todocine_webflux.entities.Premio;

public class GanadorMapper {
    public static GanadorDTO toDTO(Movie movie, Premio premio) {
        GanadorDTO ganadorDTO = new GanadorDTO();

        ganadorDTO.setPremioId(premio.getPremioId());
        ganadorDTO.setPremio(premio.getTitulo());
        ganadorDTO.setCategoriaId(premio.getCategoriaId());
        ganadorDTO.setCategoria(premio.getCategoria());
        ganadorDTO.setAnyo(premio.getAnyo());
        ganadorDTO.setMovieId(movie.getId());
        ganadorDTO.setOriginalTitle(movie.getOriginalTitle());
        ganadorDTO.setTitle(movie.getTitle());
        ganadorDTO.setOverview(movie.getOverview());
        ganadorDTO.setPosterPath(movie.getPosterPath());
        ganadorDTO.setReleaseDate(movie.getReleaseDate());

        return ganadorDTO;
    }

}


