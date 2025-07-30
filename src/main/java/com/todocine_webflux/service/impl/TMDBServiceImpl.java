package com.todocine_webflux.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todocine_webflux.service.TMDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class TMDBServiceImpl implements TMDBService {

    private static final Logger logger = LoggerFactory.getLogger(TMDBServiceImpl.class);

    @Value("${tmdb.api.token}")
    private String apiToken;

    private WebClient webClient;
    private ObjectMapper objectMapper;

    @Autowired
    public TMDBServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();

        this.objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public Mono<Map<String, Object>> getMovieById(String id) {
        String url = "/movie/" + id + "?language=es-ES&append_to_response=videos";
        return makeRequest(url);
    }

    @Override
    public Mono<Map<String, Object>> getMoviesByName(String name, Integer pagina) {
        String url = "/search/movie?query=" + name + "&include_adult=false&language=es-ES&page=" + pagina;
        return makeRequest(url);
    }

    @Override
    public Mono<Map<String, Object>> getMoviesPlayingNow(String country, Integer pagina) {
        String url = "/movie/now_playing?language=es-ES&region=" + country + "&page=" + pagina;
        return makeRequest(url);
    }

    @Override
    public Mono<Map<String, Object>> getVideosByMovieId(String movieId) {
        String url = "/movie/" + movieId + "/videos?language=es-ES&append_to_response=videos";
        return makeRequest(url);
    }

    private Mono<Map<String, Object>> makeRequest(String uri) {
        return webClient
                .get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> {
                    try {
                        logger.info("TMDB response: {}", json);
                        return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
                    } catch (Exception e) {
                        logger.error("Error parsing TMDB response", e);
                        throw new RuntimeException("Failed to parse TMDB response", e);
                    }
                });
    }
}
