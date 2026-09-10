package com.WhatsApp.AdminUserChatMessage.mapper;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.BroadCastMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.BroadCastMessageResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.BroadCastMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BroadCastMapper {

    BroadCastMessage toBroadCastMessage(BroadCastMessageRequestDto broadCastMessageRequestDto);

    BroadCastMessageResponseDto toBroadCastMessageResponseDto(BroadCastMessage broadCastMessage);

    List<BroadCastMessageResponseDto> toBroadCastMessageResponseDtoList(List<BroadCastMessage> broadCastMessages);

}
