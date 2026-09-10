package com.WhatsApp.AdminUserChatMessage.mapper;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.ConversationResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.Conversation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    ConversationResponseDto toConversationResponseDto(Conversation conversation);

    List<ConversationResponseDto> toConversationResponseDtoList(List<Conversation> conversations);

}
