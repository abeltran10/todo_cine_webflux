package com.todocine_webflux.utils.mappers;

import com.todocine_webflux.dto.response.ListaDTO;
import com.todocine_webflux.dto.response.MovieListaDTO;
import com.todocine_webflux.entities.Lista;

import java.util.List;

public class ListaMapper {

    public static ListaDTO toDTO(Lista lista) {
        ListaDTO listaDTO = new ListaDTO(lista.getId());
        listaDTO.setNombre(lista.getNombre());
        listaDTO.setDescripcion(lista.getDescripcion());

        List<MovieListaDTO> movieDTOList = lista.getMovies().stream()
                .map(movie -> {
                    MovieListaDTO movieListaDTO = new MovieListaDTO(movie.getId());
                    movieListaDTO.setTitle(movie.getTitle());
                    movieListaDTO.setPosterPath(movie.getPosterPath());
                    movieListaDTO.setReleaseDate(movie.getReleaseDate());

                    return movieListaDTO;
                }).toList();
        listaDTO.setMovies(movieDTOList);

        listaDTO.setUsername(lista.getUsername());
        listaDTO.setPublica("S".equals(lista.getPublica()));

        return listaDTO;
    }
}
