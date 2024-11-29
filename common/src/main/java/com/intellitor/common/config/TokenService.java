package com.intellitor.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class TokenService {

    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

    public String getUsername(HttpServletRequest request) {
        String payload = extractPayload(request);
        if (payload == null) {
            return null;
        }
        try {
            JSONParser parser = new JSONParser(payload);
            return (String) parser.parseObject().get("preferred_username");
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse token payload", e);
        }
    }

    private String extractPayload(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authorizationHeader.substring(7);
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }
        return new String(DECODER.decode(tokenParts[1]));
    }
}
