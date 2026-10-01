package com.WhatsApp.AdminUserChatMessage.config;

import com.WhatsApp.AdminUserChatMessage.entity.BroadCastMessage;
import com.WhatsApp.AdminUserChatMessage.entity.PresentEvent;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.entity.type.MessageType;
import com.WhatsApp.AdminUserChatMessage.entity.type.UserStatus;
import com.WhatsApp.AdminUserChatMessage.mapper.BroadCastMapper;
import com.WhatsApp.AdminUserChatMessage.repository.BroadCastRepository;
import com.WhatsApp.AdminUserChatMessage.repository.PresentEventRepository;
import com.WhatsApp.AdminUserChatMessage.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PresentEventRepository presentEventRepository;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent sessionConnectedEvent) {
        if(!(sessionConnectedEvent.getUser() instanceof Authentication auth) || !(auth.getPrincipal() instanceof User principalUser)) {
            return;
        }
        User user = userRepository.findById(principalUser.getId()).orElse(null);
        if(user == null) {
            return;
        }
        log.info("{} joined the session", user.getName());
        BroadCastMessage msg = new BroadCastMessage();
        msg.setUser(user);
        msg.setContent(user.getName()+" joined the session");
        msg.setMessageType(MessageType.JOIN);
        msg.setCreatedAt(LocalDateTime.now());
        broadCastRepository.save(msg);
        user.setStatus(UserStatus.ONLINE);
        userRepository.save(user);
        PresentEvent present = new PresentEvent();
        present.setUser(user);
        present.setMessageType(MessageType.JOIN);
        present.setCreatedAt(LocalDateTime.now());
        presentEventRepository.save(present);
        messagingTemplate.convertAndSend("/topic/public", broadCastMapper.toBroadCastMessageResponseDto(msg));
    }

    @EventListener
    public void handleWebSocketEventListener(SessionDisconnectEvent sessionDisconnectEvent) {
        if (!(sessionDisconnectEvent.getUser() instanceof Authentication auth) || !(auth.getPrincipal() instanceof User principalUser)) {
            return;
        }
        User user = userRepository.findById(principalUser.getId()).orElse(null);
        if(user == null){
            return;
        }
        log.info("{} left the session", user.getName());
        BroadCastMessage msg = new BroadCastMessage();
        msg.setUser(user);
        msg.setContent(user.getName() + " left the chat");
        msg.setMessageType(MessageType.LEFT);
        msg.setCreatedAt(LocalDateTime.now());
        broadCastRepository.save(msg);
        user.setStatus(UserStatus.OFFLINE);
        userRepository.save(user);
        PresentEvent present = new PresentEvent();
        present.setUser(user);
        present.setMessageType(MessageType.LEFT);
        present.setCreatedAt(LocalDateTime.now());
        presentEventRepository.save(present);
        messagingTemplate.convertAndSend("/topic/public", broadCastMapper.toBroadCastMessageResponseDto(msg));
    }
}


