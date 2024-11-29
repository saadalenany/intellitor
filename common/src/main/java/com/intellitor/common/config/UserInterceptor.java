package com.intellitor.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private final UserContext userContext;

    public UserInterceptor(TokenService tokenService, UserContext userContext) {
        this.tokenService = tokenService;
        this.userContext = userContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = tokenService.getUsername(request);
        userContext.setUsername(username);
        return true;
    }
}
