package com.WhatsApp.AdminUserChatMessage.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = """
                {
                    "status": 401,
                    "error":"Unauthorized",
                    "message":"%s",
                    "timestamp": %d,
                    "path":"%s"
                }
                """.formatted(authenticationException.getMessage(), System.currentTimeMillis(), request.getRequestURI());
        response.getWriter().write(json);
    }
}
