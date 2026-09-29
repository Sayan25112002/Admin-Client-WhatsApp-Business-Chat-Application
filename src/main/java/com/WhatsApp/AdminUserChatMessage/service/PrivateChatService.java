package com.WhatsApp.AdminUserChatMessage.service;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ChatMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ConversationResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.User;

import java.util.List;

public interface PrivateChatService {

    ConversationResponseDto openConversation(User requestor, Long clientId);

    List<ChatMessageResponseDto> getMessages(User requestor, Long conversationId);

}
