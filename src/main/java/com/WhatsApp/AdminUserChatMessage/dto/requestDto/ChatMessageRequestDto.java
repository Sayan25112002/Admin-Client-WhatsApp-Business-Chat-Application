package com.WhatsApp.AdminUserChatMessage.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageRequestDto {

    private Long conversationId;

    private String content;

}
