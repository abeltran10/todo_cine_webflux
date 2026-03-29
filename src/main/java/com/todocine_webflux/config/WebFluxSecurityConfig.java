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
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
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
                                                            AuthenticationWebFilter authFilter, // Tu JWTAuthenticationFilter
                                                            JwtAuthorizationWebFilter jwtFilter) { // Tu JWTAuthorisationFilter

        return http
                // 1. CORS y CSRF (Igual que en tu API normal)
                .cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // 2. Stateless en WebFlux
                // No existe SessionCreationPolicy.STATELESS como tal,
                // se desactiva el almacenamiento de contexto en sesión.
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                // 3. Manejo explícito del 401 (AuthenticationEntryPoint)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((exchange, authException) -> {
                            return Mono.fromRunnable(() ->
                                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
                            );
                        })
                )

                // 4. Autorización (authorizeExchange en lugar de authorizeHttpRequests)
                .authorizeExchange(ex -> ex
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS para CORS
                        .pathMatchers(resources).permitAll()
                        .pathMatchers(paths).permitAll()
                        .pathMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .pathMatchers(HttpMethod.POST, "/login").permitAll()
                        .anyExchange().authenticated()
                )

                // 5. Desactivar formularios y básico
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // 6. Filtros con orden correcto
                // authFilter suele ir en AUTHENTICATION y jwtFilter en AUTHORIZATION
                .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHORIZATION)

                // 7. Logout "dummy" (200 OK)
                .logout(lo -> lo
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((exchange, authentication) -> {
                            exchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
                            return Mono.empty();
                        })
                )
                .build();
    }
}
