package com.networkKnights.ECMS_Gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

@Component
public class JwtTokenCheckerFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        System.out.println(new Date().getTime() + httpRequest.getURI().toString());
        //checking url of auth because auth does not have to contain jwt token
        if (httpRequest.getURI().getPath().split("/")[1].contentEquals("auth")) {
            System.out.println("auth called");
            return chain.filter(exchange);
        } else {
            //checking url containing jwt token and have permission to access url
            if (httpRequest.getHeaders().containsKey("token") && jwtAndRoleAuthentication(httpRequest.getHeaders().get("token").get(0), httpRequest.getURI().getPath())) {
                System.out.println(httpRequest.getHeaders().get("token").get(0));
                System.out.println(exchange.getRequest().getURI() + " calling!");
                return chain.filter(exchange);
            } else {
                //if user does not have valid permission then else part will execute
                System.out.println("else \t contains key:" + httpRequest.getHeaders().containsKey("token"));
                URI redirectURI = URI.create("http://localhost:8181/auth/unauthorized");
                ServerHttpRequest modifiedRequest = exchange
                        .getRequest()
                        .mutate()
                        .uri(redirectURI)
                        .build();

                ServerWebExchange modifiedExchange = exchange
                        .mutate()
                        .request(modifiedRequest)
                        .build();
                modifiedExchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, redirectURI);
                //redirecting to unauthorized page
                return chain.filter(modifiedExchange);
            }
        }
    }

    private boolean jwtAndRoleAuthentication(String JWTToken, String path) {
        Map<String, String> token = new HashMap<>();
        token.put("token", JWTToken);
        token.put("url", path);
        System.out.println("calling");

        HttpEntity<?> httpEntity = new HttpEntity<>(token, new HttpHeaders());

        //calling validate method of auth service to validate jwt token and requested url
        boolean response = Boolean.TRUE.equals(new RestTemplate().postForObject("http://localhost:8181/auth/validate",
                httpEntity,
                Boolean.class));
        System.out.println("got response: " + response);

        return response;
    }
}

