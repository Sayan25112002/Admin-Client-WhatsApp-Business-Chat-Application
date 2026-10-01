package com.WhatsApp.AdminUserChatMessage.config;

import com.WhatsApp.AdminUserChatMessage.entity.RefreshToken;
import com.WhatsApp.AdminUserChatMessage.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import com.WhatsApp.AdminUserChatMessage.security.JwtAuthFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/generateImageCaptcha", "/validateCaptcha").permitAll()
                        .requestMatchers("/generateOtp", "/validateEmailOtp", "/validateContactOtp").permitAll()
                        .requestMatchers("/refresh-token/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .requestMatchers("/", "/login",  "/register", "/groupchat").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            String refreshTokenValue = request.getHeader("Refresh-Token");
                            if(refreshTokenValue == null || refreshTokenValue.isBlank()){
                                response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                                response.setContentType("application/json");
                                try{
                                    response.getWriter().write("""
                                            {
                                                "status":400,
                                                "message":"Refresh Token Missing"
                                            }
                                            """);
                                    response.flushBuffer();
                                }catch(Exception ex){
                                    throw new RuntimeException(ex);
                                }
                                return;
                            }
                            RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenValue)
                                    .orElse(null);
                            if(refreshToken == null){
                                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                response.setContentType("application/json");
                                try {
                                    response.getWriter().write("""
                                            {
                                                "status":404,
                                                "message":"Refresh Token Not Found"
                                            }
                                            """);
                                    response.flushBuffer();
                                }catch(Exception ex){
                                    throw new RuntimeException(ex);
                                }
                                return;
                            }
                            if(refreshToken.getRevoked()){
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.setContentType("application/json");
                                try{
                                    response.getWriter().write("""
                                            {
                                                "status":400,
                                                "message":"Refresh Token Already Revoked"
                                            }
                                            """);
                                    response.flushBuffer();
                                }catch(Exception ex){
                                    throw new RuntimeException(ex);
                                }
                                return;
                            }
                            refreshToken.setRevoked(true);
                            refreshTokenRepository.save(refreshToken);
                        })
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if(response.isCommitted()){
                                return;
                            }
                            SecurityContextHolder.clearContext();
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                                    {
                                        "status":200,
                                        "message":"Logout Successful"
                                    }
                                    """);
                            response.getWriter().flush();
                        })
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:63342"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}