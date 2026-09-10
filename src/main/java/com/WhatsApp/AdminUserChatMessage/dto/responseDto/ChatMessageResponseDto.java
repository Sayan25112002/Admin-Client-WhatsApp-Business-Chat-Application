package com.WhatsApp.AdminUserChatMessage.dto.responseDto;

import com.WhatsApp.AdminUserChatMessage.entity.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponseDto {

    private Long id;

    private Long conversationId;

    private Long senderId;

    private String content;

    private MessageType messageType;

    private LocalDateTime createdAt;

}
