package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.ChatMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ChatMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.type.Role;
import com.WhatsApp.AdminUserChatMessage.service.AuthenticationService;
import com.WhatsApp.AdminUserChatMessage.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageWebSocketController {

    private final ChatMessageService chatMessageService;
    private final AuthenticationService authenticationService;

    @MessageMapping("/private.sendMessage")
    public void sendPrivateMessage(ChatMessageRequestDto chatMessageRequestDto, Authentication authentication) {
        Long userId = authenticationService.getAuthenticatedUserIdFromToken(authentication);
        Role role = authenticationService.getAuthenticatedRoleFromToken(authentication);
        ChatMessageResponseDto chatMessageResponseDto = chatMessageService.sendMessage(chatMessageRequestDto, userId, role);
        chatMessageService.sendToConversationParticipants(chatMessageResponseDto);
    }
}
