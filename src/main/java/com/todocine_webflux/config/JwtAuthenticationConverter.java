package com.todocine_webflux.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todocine_webflux.dto.UsuarioDTO;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        if (!exchange.getRequest().getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)) {
            return Mono.empty();
        }

        return DataBufferUtils.join(exchange.getRequest().getBody())   // <‑‑ junta todos
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);                   // evita memory leak
                    try {
                        UsuarioDTO creds = mapper.readValue(bytes, UsuarioDTO.class);
                        return Mono.just(new UsernamePasswordAuthenticationToken(
                                creds.getUsername(), creds.getPassword()));
                    } catch (IOException e) {
                        return Mono.error(e);
                    }
                });
    }
}
