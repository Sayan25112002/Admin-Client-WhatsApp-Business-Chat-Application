package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.ChatMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ChatMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.Conversation;
import com.WhatsApp.AdminUserChatMessage.entity.type.Role;
import com.WhatsApp.AdminUserChatMessage.repository.ConversationRepository;
import com.WhatsApp.AdminUserChatMessage.service.AuthenticationService;
import com.WhatsApp.AdminUserChatMessage.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageWebSocketController {

    private final ChatMessageService chatMessageService;
    private final AuthenticationService authenticationService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ConversationRepository conversationRepository;

    @MessageMapping("/private.sendMessage")
    public void sendPrivateMessage(ChatMessageRequestDto chatMessageRequestDto, Authentication authentication) {
        Long userId = authenticationService.getAuthenticatedUserIdFromToken(authentication);
        Role role = authenticationService.getAuthenticatedRoleFromToken(authentication);
        ChatMessageResponseDto chatMessageResponseDto = chatMessageService.sendMessage(chatMessageRequestDto, userId, role);
        Conversation conversation = conversationRepository.findById(chatMessageResponseDto.getConversationId()).orElseThrow(()->new RuntimeException("Conversation not found"));
        simpMessagingTemplate.convertAndSendToUser(conversation.getAdmin().getUsername(), "/queue/private", chatMessageResponseDto);
        simpMessagingTemplate.convertAndSendToUser(conversation.getClient().getUsername(), "/queue/private", chatMessageResponseDto);
    }
}
