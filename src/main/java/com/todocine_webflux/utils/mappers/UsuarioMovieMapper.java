package com.todocine_webflux.utils.mappers;


import com.todocine_webflux.dto.UsuarioMovieDTO;
import com.todocine_webflux.entities.Movie;
import com.todocine_webflux.entities.Usuario;
import com.todocine_webflux.entities.UsuarioMovie;

public class UsuarioMovieMapper {

    public static UsuarioMovie toEntity(UsuarioMovieDTO usuarioMovieDTO) {
        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuarioId(usuarioMovieDTO.getUsuarioId());
        usuarioMovie.setMovieId(usuarioMovieDTO.getMovieId());
        usuarioMovie.setFavoritos(usuarioMovieDTO.getFavoritos() ? "S" : "N");
        usuarioMovie.setVista(usuarioMovieDTO.getVista() ? "S" : "N");
        usuarioMovie.setVoto(usuarioMovieDTO.getVoto());

        return usuarioMovie;
    }

    public static UsuarioMovieDTO toDTO(UsuarioMovie usuarioMovie) {
        UsuarioMovieDTO usuarioMovieDTO = new UsuarioMovieDTO();
        usuarioMovieDTO.setUsuarioId(usuarioMovie.getUsuarioId());
        usuarioMovieDTO.setMovieId(usuarioMovie.getMovieId());
        usuarioMovieDTO.setFavoritos(usuarioMovie.getFavoritos().equals("S"));
        usuarioMovieDTO.setVista(usuarioMovie.getVista().equals("S"));
        usuarioMovieDTO.setVoto(usuarioMovie.getVoto());

        return usuarioMovieDTO;
    }
}
