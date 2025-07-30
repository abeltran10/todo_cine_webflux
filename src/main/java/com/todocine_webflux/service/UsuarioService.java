package com.todocine_webflux.service;

import com.todocine_webflux.dto.UsuarioDTO;
import com.todocine_webflux.exceptions.BadRequestException;
import com.todocine_webflux.exceptions.ForbiddenException;
import com.todocine_webflux.exceptions.NotFoudException;
import reactor.core.publisher.Mono;

public interface UsuarioService {

    Mono<UsuarioDTO> getUsuarioByName (String username);

    Mono<UsuarioDTO> insertUsuario(UsuarioDTO usuarioDTO);

    Mono<UsuarioDTO> updateUsuario(String id, UsuarioDTO usuarioDTO);

    Mono<UsuarioDTO> getUsuarioById(String id);
}
