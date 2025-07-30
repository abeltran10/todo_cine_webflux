package com.todocine_webflux.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todocine_webflux.dto.UsuarioDTO;
import com.todocine_webflux.entities.Usuario;
import com.todocine_webflux.utils.mappers.UserMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.util.Date;

import static com.todocine_webflux.config.Constants.*;


public class JwtSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            UsuarioDTO dto = UserMapper.toDTO(usuario);

            String token = JWT.create()
                    .withSubject(mapper.writeValueAsString(dto))
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                    .sign(Algorithm.HMAC256(SUPER_SECRET_KEY));

            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.getHeaders().add(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + " " + token);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            DataBuffer buffer = response.bufferFactory()
                    .wrap(mapper.writeValueAsBytes(dto));

            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException ex) {
            webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return webFilterExchange.getExchange().getResponse().setComplete();
        }
    }
}
