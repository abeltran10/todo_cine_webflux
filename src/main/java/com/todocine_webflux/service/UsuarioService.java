package com.todocine_webflux.service;

import com.todocine_webflux.dto.response.UsuarioDTO;
import reactor.core.publisher.Mono;

public interface UsuarioService {

    Mono<UsuarioDTO> insertUsuario(UsuarioDTO usuarioDTO);

    Mono<UsuarioDTO> updateUsuario(String id, UsuarioDTO usuarioDTO);

    Mono<UsuarioDTO> getUsuarioById(String id);
}
