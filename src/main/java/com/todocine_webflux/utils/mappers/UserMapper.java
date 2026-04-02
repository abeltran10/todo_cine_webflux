package com.todocine_webflux.utils.mappers;

import com.todocine_webflux.dto.response.UsuarioDTO;
import com.todocine_webflux.entities.Usuario;

public class UserMapper {

    public static Usuario toEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();

        usuario.setId(usuarioDTO.getId());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setAccountNonExpired(usuarioDTO.getAccountNonExpired());
        usuario.setAccountNonLocked(usuarioDTO.getAccountNonLocked());
        usuario.setCredentialsNonExpired(usuarioDTO.getCredentialsNonExpired());
        usuario.setEnabled(usuarioDTO.getEnabled());
        usuario.setRol(usuarioDTO.getRol());

        return usuario;
    }

    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setRol(usuario.getRol());

        return usuarioDTO;
    }
}
