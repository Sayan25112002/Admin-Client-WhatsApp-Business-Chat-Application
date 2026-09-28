package com.WhatsApp.AdminUserChatMessage.service;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.ChatMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ChatMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.type.Role;

public interface ChatMessageService {

    ChatMessageResponseDto sendMessage(ChatMessageRequestDto chatMessageRequestDto, Long userId, Role role);

}
