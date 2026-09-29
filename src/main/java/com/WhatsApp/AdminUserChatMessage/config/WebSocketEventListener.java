package com.WhatsApp.AdminUserChatMessage.config;

import com.WhatsApp.AdminUserChatMessage.entity.BroadCastMessage;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.entity.type.MessageType;
import com.WhatsApp.AdminUserChatMessage.mapper.BroadCastMapper;
import com.WhatsApp.AdminUserChatMessage.repository.BroadCastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final BroadCastMapper broadCastMapper;
    private final BroadCastRepository broadCastRepository;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent sessionConnectedEvent) {
        if(!(sessionConnectedEvent.getUser() instanceof Authentication auth) || !(auth.getPrincipal() instanceof User user)) {
            return;
        }
        log.info("{} joined the session", user.getName());
        BroadCastMessage msg = new BroadCastMessage();
        msg.setUser(user);
        msg.setContent(user.getName()+"{} joined the session");
        msg.setMessageType(MessageType.JOIN);
        msg.setCreatedAt(LocalDateTime.now());
        broadCastRepository.save(msg);
        messagingTemplate.convertAndSend("/topic/public", broadCastMapper.toBroadCastMessageResponseDto(msg));
    }

    @EventListener
    public void handleWebSocketEventListener(SessionDisconnectEvent sessionDisconnectEvent) {
        if (!(sessionDisconnectEvent.getUser() instanceof Authentication auth) || !(auth.getPrincipal() instanceof User user)) {
            return;
        }
        log.info("{} left the session", user.getName());
        BroadCastMessage msg = new BroadCastMessage();
        msg.setUser(user);
        msg.setContent(user.getName() + " left the chat");
        msg.setMessageType(MessageType.LEFT);
        msg.setCreatedAt(LocalDateTime.now());
        broadCastRepository.save(msg);
        messagingTemplate.convertAndSend("/topic/public", broadCastMapper.toBroadCastMessageResponseDto(msg));
    }
}


