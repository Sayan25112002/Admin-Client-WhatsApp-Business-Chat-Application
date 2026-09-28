package com.WhatsApp.AdminUserChatMessage.config;

import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.repository.UserRepository;
import com.WhatsApp.AdminUserChatMessage.security.AuthUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor!=null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Authorization header is Invalid or Missing");
            }
            String token = authorizationHeader.substring(7);
            try {
                Claims claims = authUtil.getClaimsFromToken(token);
                String tokenType = claims.get("tokenType", String.class);
                if (!"ACCESS".equals(tokenType)) {
                    throw new IllegalArgumentException("Only ACCESS Token is allowed");
                }
                Long userId = Long.parseLong(claims.get("userId", String.class));
                User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                accessor.setUser(authentication);
                log.info("Websocket Authenticated User : {} ({})", user.getEmail(), user.getRole());
                log.info("Websocket Authorities : {}", authentication.getAuthorities());
            }catch (JwtException | IllegalArgumentException e) {
                log.error("Websocket Authentication Failed : {}", e.getMessage());
                throw new IllegalArgumentException("Invalid Websocket JWT Token");
            }
        }
        return message;
    }
}
