package com.todocine_webflux.service.impl;

import com.todocine_webflux.entities.Usuario;
import com.todocine_webflux.exceptions.ForbiddenException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

import static com.todocine_webflux.config.Constants.USER_FORBIDDEN;

public class BaseServiceImpl {

    public Mono<String> checkCurrentUser(String requestedId) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> ((Usuario) auth.getPrincipal()).getId())
                .filter(currentId -> currentId.equals(requestedId))
                .switchIfEmpty(Mono.error(new ForbiddenException(USER_FORBIDDEN)));
    }

    public Mono<Usuario> checkCurrentUser(Usuario usuario) {
        return checkCurrentUser(usuario.getId()).thenReturn(usuario);
    }

    public Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> ((Usuario) auth.getPrincipal()).getId());

    }
}
