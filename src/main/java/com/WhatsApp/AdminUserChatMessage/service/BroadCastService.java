package com.WhatsApp.AdminUserChatMessage.service;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.BroadCastMessageRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.BroadCastMessageResponseDto;

import java.nio.file.AccessDeniedException;

public interface BroadCastService {

    BroadCastMessageResponseDto sendMessage(BroadCastMessageRequestDto broadCastMessageRequestDto, Long senderId) throws AccessDeniedException;

}
