package com.WhatsApp.AdminUserChatMessage.mapper;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.ChatMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ChatMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    ChatMessageResponseDto toChatMessageResponseDto(ChatMessage chatMessage);

}
