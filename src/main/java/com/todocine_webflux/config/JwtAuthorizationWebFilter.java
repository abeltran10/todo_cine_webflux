package com.todocine_webflux.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todocine_webflux.dto.UsuarioDTO;
import com.todocine_webflux.entities.Usuario;
import com.todocine_webflux.utils.mappers.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.todocine_webflux.config.Constants.*;

public class JwtAuthorizationWebFilter implements WebFilter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String header = exchange.getRequest()
                .getHeaders()
                .getFirst(HEADER_AUTHORIZACION_KEY);

        if (header == null || !header.startsWith(TOKEN_BEARER_PREFIX)) {
            return chain.filter(exchange);
        }

        String token = header.substring(header.indexOf(' ') + 1);
        try {
            JWTVerifier verifier = JWT
                    .require(Algorithm.HMAC256(SUPER_SECRET_KEY))
                    .build();

            String usuarioString = verifier.verify(token).getSubject();
            UsuarioDTO dto = mapper.readValue(usuarioString, UsuarioDTO.class);
            Usuario usuario = UserMapper.toEntity(dto);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    usuario, usuario.getPassword(), List.of());

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

        } catch (JWTVerificationException | JsonProcessingException ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
