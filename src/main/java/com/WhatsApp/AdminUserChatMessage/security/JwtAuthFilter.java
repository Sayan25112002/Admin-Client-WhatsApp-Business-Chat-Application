package com.WhatsApp.AdminUserChatMessage.security;

import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    private void writeErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("""
                {
                    "status": 401,
                    "error":"Unauthorized",
                    "message":"%s",
                    "timestamp": %d,
                    "path":"%s"
                }
                """.formatted(message, System.currentTimeMillis(), req.getRequestURI())
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.startsWith("/auth/")){
            filterChain.doFilter(request,response);
            return;
        }
        log.info("Incoming request URI : {}", uri);
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            writeErrorMessage(request,response,"Invalid JWT Token");
            return;
        }
        final String token = authorizationHeader.substring(7);
        try {
            Claims claims = authUtil.getClaimsFromToken(token);
            String tokenType = claims.get("tokenType").toString();
            if (!tokenType.equals("ACCESS")) {
                writeErrorMessage(request, response, "Only Access Token Allowed");
                return;
            }
            Long userId = Long.parseLong(claims.get("userId").toString());
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("No User found with userId: " + userId));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException e){
            writeErrorMessage(request, response, "Jwt Token Expired");
        }catch (JwtException e){
            writeErrorMessage(request, response, "Invalid JWT Token");
        }
    }
}

