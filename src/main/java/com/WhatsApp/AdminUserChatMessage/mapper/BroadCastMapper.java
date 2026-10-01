package com.WhatsApp.AdminUserChatMessage.mapper;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.BroadCastMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.BroadCastMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.BroadCastMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BroadCastMapper {

    BroadCastMessage toBroadCastMessage(BroadCastMessageRequestDto broadCastMessageRequestDto);

    @Mapping(source = "user.name", target = "senderName")
    BroadCastMessageResponseDto toBroadCastMessageResponseDto(BroadCastMessage broadCastMessage);

}
