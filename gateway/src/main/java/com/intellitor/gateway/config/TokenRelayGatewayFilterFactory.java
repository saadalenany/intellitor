//package com.intellitor.gateway.config;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TokenRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
//
//    @Override
//    public GatewayFilter apply(Object config) {
//        return (exchange, chain) -> exchange.getPrincipal()
//                .map(principal -> exchange.getRequest().mutate()
//                        .header("Authorization", "Bearer " + ((OAuth2AuthenticationToken) principal).getAuthorizedClientRegistrationId())
//                        .build())
//                .map(request -> exchange.mutate().request(request).build())
//                .flatMap(chain::filter);
//    }
//}
