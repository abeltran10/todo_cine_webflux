package com.todocine_webflux.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;


import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@Configuration
public class WebFluxSecurityConfig {

    @Value("${permit.all.resources}")
    private String[] resources;

    @Value("${permit.all.paths}")
    private String[] paths;

    private final ReactiveUserDetailsService usuarioService;

    public WebFluxSecurityConfig(ReactiveUserDetailsService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** AuthenticationManager reactivo basado en tu servicio de usuarios */
    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(usuarioService);
        manager.setPasswordEncoder(passwordEncoder());
        return manager;
    }

    /** Filtro de autenticación (login) */
    @Bean
    public AuthenticationWebFilter jwtAuthenticationWebFilter(ReactiveAuthenticationManager manager) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        AuthenticationWebFilter filter = new AuthenticationWebFilter(manager);
        filter.setServerAuthenticationConverter(converter);        // lee username/password del body JSON
        filter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/login"));
        filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
        filter.setAuthenticationFailureHandler((exchange, exception) ->
                Mono.fromRunnable(() ->
                        exchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)));

        return filter;
    }

    /** Filtro de autorización (todas las peticiones salvo login y los permitAll) */
    @Bean
    public JwtAuthorizationWebFilter jwtAuthorizationWebFilter() {
        return new JwtAuthorizationWebFilter();
    }

    /** Cadena de filtros y reglas */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            AuthenticationWebFilter authFilter,
                                                            JwtAuthorizationWebFilter jwtFilter) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(ex -> ex
                        .pathMatchers(resources).permitAll()
                        .pathMatchers(paths).permitAll()
                        .pathMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .pathMatchers(HttpMethod.POST, "/login").permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .logout(lo -> lo.logoutUrl("/logout"))
                .build();
    }
}
