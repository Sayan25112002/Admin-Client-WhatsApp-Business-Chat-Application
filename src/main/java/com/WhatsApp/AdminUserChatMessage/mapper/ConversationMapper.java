package com.WhatsApp.AdminUserChatMessage.mapper;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ConversationResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.Conversation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    ConversationResponseDto toConversationResponseDto(Conversation conversation);

}
