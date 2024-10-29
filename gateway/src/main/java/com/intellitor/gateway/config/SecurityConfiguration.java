package com.intellitor.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Value("${custom.redirect.uri}")
    private String redirectUri;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("/unauthenticated", "/oauth2/**", "/login/**", "/public/**").permitAll()
                .anyExchange().authenticated()
        )
        .oauth2Login(oauth2Login -> oauth2Login
                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler(redirectUri))
        );
        return http.build();
    }
}