//package com.intellitor.user.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
//import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration {
//
//    @Value("${keycloak.url}")
//    private String keycloakUrl;
//
//    @Value("${keycloak.realm}")
//    private String keycloakRealm;
//
//    @Value("${keycloak.valid-redirect-uri}")
//    private String validRedirectUri;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.oauth2Login(oauth2Login -> oauth2Login
//                .userInfoEndpoint(userInfo -> userInfo.userAuthoritiesMapper(grantedAuthoritiesMapper()))
//        );
//        http.sessionManagement(sessions -> sessions
//                .requireExplicitAuthenticationStrategy(true)
//        );
//        http.authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/unauthenticated", "/oauth2/**", "/login/**")
//                        .permitAll()
//                        .anyRequest()
//                        .fullyAuthenticated())
//                .logout(logout -> logout.logoutSuccessUrl(
//                        String.format("%s/realms/%s/protocol/openid-connect/logout?redirect_uri=%s/", keycloakUrl, keycloakRealm, validRedirectUri)
//                ));
//        return http.build();
//    }
//
//    private GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
//        return authorities -> {
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//            authorities.forEach(authority -> {
//                GrantedAuthority mappedAuthority;
//                if (authority instanceof OidcUserAuthority oidcUserAuthority) {
//                    mappedAuthority = new OidcUserAuthority("OIDC_USER", oidcUserAuthority.getIdToken(), oidcUserAuthority.getUserInfo());
//                } else if (authority instanceof OAuth2UserAuthority oidcUserAuthority) {
//                    mappedAuthority = new OAuth2UserAuthority("OAUTH2_USER", oidcUserAuthority.getAttributes());
//                } else {
//                    mappedAuthority = authority;
//                }
//                mappedAuthorities.add(mappedAuthority);
//            });
//            return mappedAuthorities;
//        };
//    }
//}
