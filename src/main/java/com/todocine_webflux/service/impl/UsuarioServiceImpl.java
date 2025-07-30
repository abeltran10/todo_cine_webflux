package com.todocine_webflux.service.impl;

import com.todocine_webflux.dao.UsuarioDAO;
import com.todocine_webflux.dto.UsuarioDTO;
import com.todocine_webflux.entities.Usuario;
import com.todocine_webflux.exceptions.BadRequestException;
import com.todocine_webflux.exceptions.NotFoudException;
import com.todocine_webflux.service.UsuarioService;
import com.todocine_webflux.utils.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

import static com.todocine_webflux.config.Constants.*;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl implements ReactiveUserDetailsService, UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private UsuarioDAO usuarioDAO;

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("findByUsername -> {}", username);
        return usuarioDAO.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(USER_PASSWORD_ERROR)))
                .cast(UserDetails.class);
    }


    @Override
    public Mono<UsuarioDTO> getUsuarioByName(String username) {
        log.info("getUsuarioByName -> {}", username);

        return usuarioDAO.findByUsername(username)
                .switchIfEmpty(Mono.error(new NotFoudException(USER_NOTFOUND)))
                .flatMap(this::checkCurrentUser) // verificación de autorización
                .map(UserMapper::toDTO);
    }

    @Override
    public Mono<UsuarioDTO> insertUsuario(UsuarioDTO dto) {
        return usuarioDAO.findByUsername(dto.getUsername())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BadRequestException(USER_EXISTS));
                    }
                    Usuario usuario = new Usuario(
                            dto.getUsername(),
                            passwordEncoder().encode(dto.getPassword()));
                    return usuarioDAO.save(usuario);
                })
                .map(UserMapper::toDTO);
    }


    @Override
    public Mono<UsuarioDTO> updateUsuario(String id, UsuarioDTO dto) {
        log.info("updateUsuario -> {}", id);

        return checkCurrentUser(id)
                .flatMap(usuarioDAO::findById)
                .switchIfEmpty(Mono.error(new NotFoudException(USER_NOTFOUND)))
                .flatMap(u -> {
                    u.setPassword(passwordEncoder().encode(dto.getPassword()));
                    return usuarioDAO.save(u);
                })
                .map(UserMapper::toDTO);
    }

    @Override
    public Mono<UsuarioDTO> getUsuarioById(String id) {
        return checkCurrentUser(id)
                .flatMap(usuarioDAO::findById)
                .switchIfEmpty(Mono.error(new NotFoudException(USER_NOTFOUND)))
                .map(UserMapper::toDTO);
    }

}
