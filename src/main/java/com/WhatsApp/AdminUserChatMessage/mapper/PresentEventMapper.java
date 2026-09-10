package com.WhatsApp.AdminUserChatMessage.mapper;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.PresentEventResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.PresentEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PresentEventMapper {

    PresentEventResponseDto toPresentEventResponseDto(PresentEvent presentEvent);

    List<PresentEventResponseDto> toPresentEventResponseDtoList(List<PresentEvent> presentEvents);

}
