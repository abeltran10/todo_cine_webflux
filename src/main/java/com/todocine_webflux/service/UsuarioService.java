package com.todocine_webflux.service;

import com.todocine_webflux.dto.request.UsuarioReqDTO;
import com.todocine_webflux.dto.response.UsuarioDTO;
import reactor.core.publisher.Mono;

public interface UsuarioService {

    Mono<UsuarioDTO> insertUsuario(UsuarioReqDTO usuarioDTO);

    Mono<UsuarioDTO> updateUsuario(String id, UsuarioReqDTO usuarioDTO);

    Mono<UsuarioDTO> getUsuarioById(String id);
}
