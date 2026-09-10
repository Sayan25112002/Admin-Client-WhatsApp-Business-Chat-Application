package com.WhatsApp.AdminUserChatMessage.mapper;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.ChatMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ChatMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    ChatMessage toChatMessageDto(ChatMessageRequestDto chatMessageRequestDto);

    ChatMessageResponseDto toChatMessageResponseDto(ChatMessage chatMessage);

    List<ChatMessageResponseDto> toChatMessageResponseDtoList(List<ChatMessage> chatMessages);

}
