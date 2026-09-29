package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ChatMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ConversationResponseDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.MemberResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.ChatMessage;
import com.WhatsApp.AdminUserChatMessage.entity.Conversation;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.entity.type.Role;
import com.WhatsApp.AdminUserChatMessage.repository.ConversationRepository;
import com.WhatsApp.AdminUserChatMessage.repository.UserRepository;
import com.WhatsApp.AdminUserChatMessage.service.PrivateChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateChatServiceImpl implements PrivateChatService {

    private static final String NO_ACCESS = "You do not have permission to access this conversation";
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final SimpUserRegistry simpUserRegistry;

    @Override
    public List<MemberResponseDto> getMembers() {
        Set<String> onlineNames = simpUserRegistry.getUsers().stream()
                .map(SimpUser::getName)
                .collect(Collectors.toSet());
        return userRepository.findAll().stream()
                .map(user -> MemberResponseDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .role(user.getRole().toString())
                        .online(onlineNames.contains(user.getUsername()))
                        .build())
                .toList();
    }

    @Override
    public ConversationResponseDto openConversation(User requestor, Long clientId) {
        User admin,client;
        if(requestor.getRole()== Role.ADMIN) {
            if (clientId == null) {
                throw new IllegalArgumentException("Client Id is Required");
            }
            admin = userRepository.findById(requestor.getId()).orElseThrow(() -> new AccessDeniedException(NO_ACCESS));
            client = userRepository.findById(clientId).orElseThrow(() -> new AccessDeniedException(NO_ACCESS));
            if (client.getRole() != Role.CLIENT) {
                throw new AccessDeniedException("Private Chat is only allowed between CLIENT and ADMIN");
            }
        }
        else if(requestor.getRole()== Role.CLIENT) {
            client = userRepository.findById(requestor.getId()).orElseThrow(() -> new AccessDeniedException(NO_ACCESS));
            admin = userRepository.findByRole(Role.ADMIN).orElseThrow(() -> new RuntimeException("Admin Not Found"));
        }else  {
            throw new AccessDeniedException("Invalid User Role");
        }
        Conversation conversation = conversationRepository.findByAdminIdAndClientId(admin.getId(), client.getId())
                .orElseGet(()-> {
                    LocalDateTime now = LocalDateTime.now();
                    Conversation c = new Conversation();
                    c.setAdmin(admin);
                    c.setClient(client);
                    c.setCreatedAt(now);
                    c.setUpdatedAt(now);
                    return conversationRepository.save(c);
                });
        return ConversationResponseDto.builder()
                .id(conversation.getId())
                .adminId(admin.getId())
                .clientId(client.getId())
                .createdAt(conversation.getCreatedAt())
                .build();
    }

    @Override
    public List<ChatMessageResponseDto> getMessages(User requestor, Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() -> new AccessDeniedException(NO_ACCESS));
        Boolean participant = conversation.getAdmin().getId().equals(requestor.getId())||conversation.getClient().getId().equals(requestor.getId());
        if(!participant) {
            throw new AccessDeniedException(NO_ACCESS);
        }
        return conversation.getChatMessages().stream()
                .sorted(Comparator.comparing(ChatMessage::getId))
                .map(m->ChatMessageResponseDto.builder()
                        .id(m.getId())
                        .conversationId(conversation.getId())
                        .senderId(m.getUser().getId())
                        .senderName(m.getUser().getName())
                        .senderRole(m.getUser().getRole().toString())
                        .content(m.getContent())
                        .messageType(m.getMessageType())
                        .createdAt(m.getCreatedAt())
                        .build())
                .toList();

    }
}
