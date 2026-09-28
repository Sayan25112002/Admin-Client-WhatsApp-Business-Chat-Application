package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.ChatMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ChatMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.ChatMessage;
import com.WhatsApp.AdminUserChatMessage.entity.Conversation;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.entity.type.MessageType;
import com.WhatsApp.AdminUserChatMessage.entity.type.Role;
import com.WhatsApp.AdminUserChatMessage.repository.ChatMessageRepository;
import com.WhatsApp.AdminUserChatMessage.repository.ConversationRepository;
import com.WhatsApp.AdminUserChatMessage.repository.UserRepository;
import com.WhatsApp.AdminUserChatMessage.service.ChatMessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ChatMessageResponseDto sendMessage(ChatMessageRequestDto chatMessageRequestDto, Long userId, Role role) {
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Conversation conversation;
        if(role == Role.CLIENT) {
            User admin = userRepository.findByRole(Role.ADMIN)
                    .orElseThrow(()-> new RuntimeException("Admin not found"));
            conversation = conversationRepository.findByAdminIdAndClientId(admin.getId(),user.getId()).orElse(null);
            if(conversation == null) {
                conversation = new Conversation();
                conversation.setAdmin(admin);
                conversation.setClient(user);
                conversation.setCreatedAt(now);
                conversation.setUpdatedAt(now);
                conversationRepository.save(conversation);
            }
        }else if(role == Role.ADMIN) {
            conversation = conversationRepository.findByIdAndAdminId(chatMessageRequestDto.getConversationId(),userId)
                    .orElseThrow(() -> new AccessDeniedException("You do not have permission to access this conversation"));
        }
        else {
            throw new AccessDeniedException("Invalid User Role");
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setConversation(conversation);
        chatMessage.setUser(user);
        chatMessage.setContent(chatMessageRequestDto.getContent());
        chatMessage.setMessageType(MessageType.CHAT);
        chatMessage.setCreatedAt(now);
        chatMessageRepository.save(chatMessage);
        conversation.setUpdatedAt(now);
        return ChatMessageResponseDto.builder()
                .id(chatMessage.getId())
                .conversationId(conversation.getId())
                .senderId(user.getId())
                .content(chatMessage.getContent())
                .messageType(chatMessage.getMessageType())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
